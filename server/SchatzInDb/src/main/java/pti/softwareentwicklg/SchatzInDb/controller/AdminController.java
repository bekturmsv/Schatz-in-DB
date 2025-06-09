package pti.softwareentwicklg.SchatzInDb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import pti.softwareentwicklg.SchatzInDb.dto.TeacherDto;
import pti.softwareentwicklg.SchatzInDb.dto.request.RegisterTeacherRequest;
import pti.softwareentwicklg.SchatzInDb.model.enums.Roles;
import pti.softwareentwicklg.SchatzInDb.model.user.Teacher;
import pti.softwareentwicklg.SchatzInDb.repository.user.TeacherRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;

@RequestMapping("/api/admin")
@RestController
public class AdminController {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;


    public AdminController(UserRepository userRepository, TeacherRepository teacherRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
}