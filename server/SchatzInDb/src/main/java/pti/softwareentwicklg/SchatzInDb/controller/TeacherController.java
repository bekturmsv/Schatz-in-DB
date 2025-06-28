package pti.softwareentwicklg.SchatzInDb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pti.softwareentwicklg.SchatzInDb.model.user.Teacher;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        List<User> listTeacher = new ArrayList<>();

        List<User> users = userRepository.findAll();

        for (User user: users) {
            if (user instanceof Teacher teacher) {
                listTeacher.add(teacher);
            }
        }

        return ResponseEntity.ok(listTeacher);
    }
}
