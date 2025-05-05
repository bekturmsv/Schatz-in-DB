package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.RegisterTeacherRequest;
import com.prog.datenbankspiel.dto.TeacherDto;
import com.prog.datenbankspiel.model.user.Teacher;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-04T22:56:26+0200",
    comments = "version: 1.6.1, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
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
        teacher.setSubject( request.getSubject() );

        return teacher;
    }

    @Override
    public void update(RegisterTeacherRequest request, Teacher teacher) {
        if ( request == null ) {
            return;
        }

        teacher.setUsername( request.getUsername() );
        teacher.setPassword( request.getPassword() );
        teacher.setEmail( request.getEmail() );
        teacher.setSubject( request.getSubject() );
    }
}
