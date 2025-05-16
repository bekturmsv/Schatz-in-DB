package com.prog.datenbankspiel.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TeacherDto {

    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String subject;
}
