package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.ChangeGroupRequest;
import com.prog.datenbankspiel.dto.ChangePasswordRequest;
import com.prog.datenbankspiel.dto.profile.AdminUpdatePlayerDto;
import com.prog.datenbankspiel.dto.profile.AdminUpdateTeacherDto;
import com.prog.datenbankspiel.dto.profile.PlayerProfileUpdateDto;
import com.prog.datenbankspiel.model.user.Group;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.Teacher;
import com.prog.datenbankspiel.model.user.User;
import com.prog.datenbankspiel.model.user.enums.Roles;
import com.prog.datenbankspiel.repository.user.GroupRepository;
import com.prog.datenbankspiel.repository.user.PlayerRepository;
import com.prog.datenbankspiel.repository.user.TeacherRepository;
import com.prog.datenbankspiel.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(Authentication authentication,
                                                 @RequestBody ChangePasswordRequest request) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Wrong old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Password updated");
    }

    @Transactional
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteAccount(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
        return ResponseEntity.ok("User deleted");
    }

    @PutMapping("/group")
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<String> changeGroup(@RequestBody ChangeGroupRequest request, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(user instanceof Player player)) {
            return ResponseEntity.status(403).body("Only players can change group");
        }

        Group group = groupRepository.findByCode(request.getGroupCode())
                .orElse(null);

        if (group == null) return ResponseEntity.badRequest().body("Group not found");

        player.setGroupId(group);
        playerRepository.save(player);

        return ResponseEntity.ok("Group changed successfully");
    }

    @PutMapping("/player/{id}")
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<?> updatePlayerProfile(@PathVariable Long id,
                                                 @RequestBody PlayerProfileUpdateDto dto,
                                                 Authentication auth) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        if (!player.getUsername().equals(auth.getName())) {
            return ResponseEntity.status(403).body("Cannot edit another player's profile");
        }

        player.setFirstName(dto.getFirstName());
        player.setLastName(dto.getLastName());
        player.setUsername(dto.getUsername());
        player.setEmail(dto.getEmail());

        return ResponseEntity.ok(playerRepository.save(player));
    }

    @PutMapping("/admin/teacher/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> updateTeacher(@PathVariable Long id,
                                           @RequestBody AdminUpdateTeacherDto dto) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setSubject(dto.getSubject());

        var groups = groupRepository.findAllById(dto.getGroupIds());
        groups.forEach(group -> group.setTeacher(teacher));
        groupRepository.saveAll(groups);

        return ResponseEntity.ok(teacherRepository.save(teacher));
    }

    @PutMapping("/admin/player/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePlayerByAdmin(@PathVariable Long id,
                                                 @RequestBody AdminUpdatePlayerDto dto) {
        var player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        var group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        player.setFirstName(dto.getFirstName());
        player.setLastName(dto.getLastName());
        player.setEmail(dto.getEmail());
        player.setGroupId(group);

        return ResponseEntity.ok(playerRepository.save(player));
    }
}
