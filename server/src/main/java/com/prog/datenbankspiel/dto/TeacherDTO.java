package com.prog.datenbankspiel.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TeacherDTO {

    private String username;

    private String password;

    private String email;

    private String first_name;

    private String last_name;

    private Long subject;
}
