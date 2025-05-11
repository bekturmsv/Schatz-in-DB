package com.prog.datenbankspiel.dto;

import lombok.Data;

@Data
public class PlayerDto {

    private Long playerId;

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;

    private Long totalPoints;
    private Long levelId;
    private String design;
    private String specialistGroup;
    private int matriculationNumber;
    private Long groupId;
}

