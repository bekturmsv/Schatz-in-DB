package pti.softwareentwicklg.SchatzInDb.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pti.softwareentwicklg.SchatzInDb.model.enums.Roles;
import pti.softwareentwicklg.SchatzInDb.model.user.Teacher;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.user.TeacherRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;

@Component
@RequiredArgsConstructor
public class TeacherInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeacherRepository teacherRepository;

    @Value("${app.teacher.username}")
    private String teacherUsername;
    @Value("${app.teacher.password}")
    private String teacherPassword;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(teacherUsername).isEmpty()) {
            Teacher teacher = new Teacher();
            teacher.setUsername(teacherUsername);
            teacher.setFirstName("John");
            teacher.setLastName("Doe");
            teacher.setEmail("JohnDoe@gmail.com");
            teacher.setPassword(passwordEncoder.encode(teacherPassword));
            teacher.setRole(Roles.TEACHER);
            teacher.setSubject("Computer science");
            teacherRepository.save(teacher);
        }
    }
}
