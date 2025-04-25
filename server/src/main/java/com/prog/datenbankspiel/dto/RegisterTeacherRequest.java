package com.prog.datenbankspiel.dto;

import lombok.Data;

@Data
public class RegisterTeacherRequest {
    private String username;
    private String email;
    private String password;
    private String first_name;
    private String last_name;
    private Long subject;
}
