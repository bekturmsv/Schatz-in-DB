package pti.softwareentwicklg.SchatzInDb.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pti.softwareentwicklg.SchatzInDb.dto.AdminUpdatePlayerDto;
import pti.softwareentwicklg.SchatzInDb.dto.AdminUpdateTeacherDto;
import pti.softwareentwicklg.SchatzInDb.dto.request.RegisterTeacherRequest;
import pti.softwareentwicklg.SchatzInDb.model.enums.Roles;
import pti.softwareentwicklg.SchatzInDb.model.user.Group;
import pti.softwareentwicklg.SchatzInDb.model.user.Player;
import pti.softwareentwicklg.SchatzInDb.model.user.Teacher;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.user.GroupRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.PlayerRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.TeacherRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;

import java.util.List;

@RequestMapping("/api/admin")
@RestController
public class AdminController {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, PlayerRepository playerRepository, GroupRepository groupRepository, TeacherRepository teacherRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.groupRepository = groupRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/createTeacher")
    public ResponseEntity<?> createTeacher(@RequestBody RegisterTeacherRequest request,
            UriComponentsBuilder uriBuilder) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null);
        }
        Teacher teacher = new Teacher();
        teacher.setUsername( request.getUsername() );
        teacher.setEmail( request.getEmail() );
        teacher.setFirstName( request.getFirstName() );
        teacher.setLastName( request.getLastName() );
        teacher.setSubject( request.getSubject() );
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));
        teacher.setRole(Roles.TEACHER);
        teacherRepository.save(teacher);

        return ResponseEntity.ok(teacher);
    }

    @PutMapping("/update/teacher/{id}")
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
        for (Group group : groups) {
            group.setTeacher(teacher);
        }
        groupRepository.saveAll(groups);

        return ResponseEntity.ok(teacherRepository.save(teacher));
    }

    @PutMapping("/update/player/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePlayerByAdmin(@PathVariable Long id,
                                                 @RequestBody AdminUpdatePlayerDto dto) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        player.setFirstName(dto.getFirstName());
        player.setLastName(dto.getLastName());
        player.setEmail(dto.getEmail());
        player.setGroupId(group);

        return ResponseEntity.ok(playerRepository.save(player));
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}