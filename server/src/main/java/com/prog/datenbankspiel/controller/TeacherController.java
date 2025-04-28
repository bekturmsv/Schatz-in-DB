package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.RegisterTeacherRequest;
import com.prog.datenbankspiel.dto.TeacherDto;
import com.prog.datenbankspiel.mappers.TeacherMapper;
import com.prog.datenbankspiel.model.user.Teacher;
import com.prog.datenbankspiel.repository.user.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller("api/teachers")
public class TeacherController {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Iterable<TeacherDto> getAllTeachers() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable Long id) {
        Teacher teacher = teacherRepository.findById(id).orElse(null);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teacherMapper.toDto(teacher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDto> updateTeacher(@PathVariable Long id, @RequestBody RegisterTeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id).orElse(null);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        teacherMapper.update(request, teacher);
        teacherRepository.save(teacher);

        return ResponseEntity.ok(teacherMapper.toDto(teacher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        Teacher teacher = teacherRepository.findById(id).orElse(null);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        teacherRepository.delete(teacher);
        return ResponseEntity.noContent().build();
    }
}
