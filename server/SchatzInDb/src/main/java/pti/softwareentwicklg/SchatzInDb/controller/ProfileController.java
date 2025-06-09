package pti.softwareentwicklg.SchatzInDb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pti.softwareentwicklg.SchatzInDb.dto.PlayerProfileUpdateDto;
import pti.softwareentwicklg.SchatzInDb.model.user.Player;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.user.GroupRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.PlayerRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.TeacherRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;
import pti.softwareentwicklg.SchatzInDb.service.user.PlayerService;

import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlayerService playerService;

    public ProfileController(UserRepository userRepository, PlayerRepository playerRepository, TeacherRepository teacherRepository, GroupRepository groupRepository, PasswordEncoder passwordEncoder, PlayerService playerService) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.teacherRepository = teacherRepository;
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
        this.playerService = playerService;
    }

    @GetMapping("/player/getAuthorizedUser")
    public ResponseEntity<?> getById(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(playerService.getById(user));
    }

    //Тут ошибка если чтото не придет то будет null
    @PutMapping("/player/update/{id}")
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
        player.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        return ResponseEntity.ok(playerRepository.save(player));
    }
}
