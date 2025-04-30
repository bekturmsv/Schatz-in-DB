package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.ChangeGroupRequest;
import com.prog.datenbankspiel.dto.ChangePasswordRequest;
import com.prog.datenbankspiel.model.user.Group;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.enums.Roles;
import com.prog.datenbankspiel.repository.user.GroupRepository;
import com.prog.datenbankspiel.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(Authentication authentication, @RequestBody ChangePasswordRequest request) {
        var user = userRepository.findByUsername(authentication.getName()).orElseThrow();

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Wrong old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Password updated");
    }

    @Transactional
    @DeleteMapping
    public ResponseEntity<String> deleteAccount(Authentication authentication) {
        var user = userRepository.findByUsername(authentication.getName()).orElseThrow();

        userRepository.delete(user);
        return ResponseEntity.ok("User deleted");
    }

    @PutMapping("/group")
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<String> changeGroup(@RequestBody ChangeGroupRequest request, Authentication authentication) {
        String username = authentication.getName();

        var userOpt = userRepository.findByUsernameAndRole(username, Roles.PLAYER);
        if (userOpt.isEmpty()) return ResponseEntity.status(403).body("Only players can change group");

        var user = userOpt.get();
        if (!(user instanceof Player player)) {
            return ResponseEntity.status(500).body("User is not a player");
        }

        Group group = groupRepository.findAll().stream()
                .filter(g -> g.getCode().equals(request.getGroupCode()))
                .findFirst()
                .orElse(null);

        if (group == null) return ResponseEntity.badRequest().body("Group not found");

        player.setGroupId(group);
        userRepository.save(player);

        return ResponseEntity.ok("Group changed successfully");
    }

}
