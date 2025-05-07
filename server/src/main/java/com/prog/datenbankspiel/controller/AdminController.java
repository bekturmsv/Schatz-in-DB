package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.RegisterTeacherRequest;
import com.prog.datenbankspiel.dto.TeacherDto;
import com.prog.datenbankspiel.mappers.TeacherMapper;
import com.prog.datenbankspiel.repository.user.TeacherRepository;
import com.prog.datenbankspiel.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import static com.prog.datenbankspiel.model.user.enums.Roles.TEACHER;


@RequestMapping("api/admin")
@RestController
@AllArgsConstructor
public class AdminController {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @PostMapping("/createTeacher")
    public ResponseEntity<TeacherDto> createTeacher(
            @RequestBody RegisterTeacherRequest request,
            UriComponentsBuilder uriBuilder) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null);
        }
        var teacher = teacherMapper.fromRegisterRequest(request);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacher.setRole(TEACHER);
        teacherRepository.save(teacher);

        var teacherDto = teacherMapper.toDto(teacher);
        var uri = uriBuilder.path("/teachers/{id}").buildAndExpand(teacherDto.getId()).toUri();

        return ResponseEntity.created(uri).body(teacherDto);
    }
}
