package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.RegisterTeacherRequest;
import com.prog.datenbankspiel.dto.TeacherDto;
import com.prog.datenbankspiel.model.user.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    TeacherDto toDto(Teacher teacher);
    Teacher fromRegisterRequest(RegisterTeacherRequest request);
    void update(RegisterTeacherRequest request, @MappingTarget Teacher teacher);
}
