package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.RegisterTeacherRequest;
import com.prog.datenbankspiel.dto.TeacherDto;
import com.prog.datenbankspiel.model.user.Teacher;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-28T15:57:52+0200",
    comments = "version: 1.6.1, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class TeacherMapperImpl implements TeacherMapper {

    @Override
    public TeacherDto toDto(Teacher teacher) {
        if ( teacher == null ) {
            return null;
        }

        TeacherDto.TeacherDtoBuilder teacherDto = TeacherDto.builder();

        teacherDto.id( teacher.getId() );
        teacherDto.username( teacher.getUsername() );
        teacherDto.email( teacher.getEmail() );
        teacherDto.first_name( teacher.getFirstName() );
        teacherDto.last_name( teacher.getLastName() );
        teacherDto.subject( teacher.getSubject() );

        return teacherDto.build();
    }

    @Override
    public Teacher fromRegisterRequest(RegisterTeacherRequest request) {
        if ( request == null ) {
            return null;
        }

        Teacher teacher = new Teacher();

        teacher.setUsername( request.getUsername() );
        teacher.setPassword( request.getPassword() );
        teacher.setEmail( request.getEmail() );
        teacher.setFirstName( request.getFirst_name() );
        teacher.setLastName( request.getLast_name() );
        teacher.setSubject( request.getSubject() );

        return teacher;
    }
}
