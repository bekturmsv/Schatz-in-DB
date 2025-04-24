package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.TeacherDTO;
import com.prog.datenbankspiel.service.TeacherServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/admin")
@Controller
@AllArgsConstructor
public class AdminController {

    private final TeacherServiceImpl teacherServiceImpl;

    // method to create new account for teacher
    // admin access only
    @PostMapping("/createTeacher")
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO) {

        TeacherDTO teacherCreatedDTO = teacherServiceImpl.createTeacher(teacherDTO);
        return ResponseEntity.ok().body(teacherCreatedDTO);

    }
}
