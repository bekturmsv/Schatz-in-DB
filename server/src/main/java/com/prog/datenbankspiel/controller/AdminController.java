package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.RegisterTeacherRequest;
import com.prog.datenbankspiel.dto.TeacherDto;
import com.prog.datenbankspiel.mappers.TeacherMapper;
import com.prog.datenbankspiel.repository.user.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RequestMapping("api/admin")
@RestController
@AllArgsConstructor
public class AdminController {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;

    @PostMapping("/createTeacher")
    public ResponseEntity<TeacherDto> createTeacher(
            @RequestBody RegisterTeacherRequest request,
            UriComponentsBuilder uriBuilder) {
        var teacher = teacherMapper.dtoToTeacher(request);
        teacherRepository.save(teacher);

        var teacherDto = teacherMapper.teacherToDto(teacher);
        var uri = uriBuilder.path("/teachers/{id}").buildAndExpand(teacherDto.getId()).toUri();

        return ResponseEntity.created(uri).body(teacherDto);
    }
}
