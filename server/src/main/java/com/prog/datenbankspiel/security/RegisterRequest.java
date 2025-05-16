package com.prog.datenbankspiel.security;

import com.prog.datenbankspiel.model.user.enums.SpecialistGroup;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Integer matriculationNumber;
    private SpecialistGroup specialistGroup;
}