package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.GroupDTO;
import com.prog.datenbankspiel.dto.TeacherDTO;
import com.prog.datenbankspiel.model.user.Teacher;

import java.util.List;

public interface TeacherService {
    // TODO CRUD teacher service
    TeacherDTO createTeacher(TeacherDTO teacherCreateDTO);

    GroupDTO createGroup(GroupDTO groupCreateDTO);
}
