package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.GroupDTO;
import com.prog.datenbankspiel.service.TeacherServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/teacher")
@Controller
public class TeacherController {

    private final TeacherServiceImpl teacherServiceImpl;

    public TeacherController(TeacherServiceImpl teacherServiceImpl) {
        this.teacherServiceImpl = teacherServiceImpl;
    }

    // TODO create classrooms
    @PostMapping("/createGroup")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO ) {
        GroupDTO groupCreatedDTO = teacherServiceImpl.createGroup(groupDTO);
        return ResponseEntity.ok().body(groupCreatedDTO);
    }
}
