package com.prog.datenbankspiel.dto.profile;

import lombok.Data;

@Data
public class PlayerProfileUpdateDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
}
