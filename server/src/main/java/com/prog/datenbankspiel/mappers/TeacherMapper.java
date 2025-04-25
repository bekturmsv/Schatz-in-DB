package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.RegisterTeacherRequest;
import com.prog.datenbankspiel.dto.TeacherDto;
import com.prog.datenbankspiel.model.user.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    TeacherDto teacherToDto(Teacher teacher);
    Teacher dtoToTeacher(RegisterTeacherRequest teacherDto);
}
