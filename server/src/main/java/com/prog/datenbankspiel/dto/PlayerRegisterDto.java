package com.prog.datenbankspiel.dto;

import lombok.Data;

@Data
public class PlayerRegisterDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String nickname;
    private String role;
}
