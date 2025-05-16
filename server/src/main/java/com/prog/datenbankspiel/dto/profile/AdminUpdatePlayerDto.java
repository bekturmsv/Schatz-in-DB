package com.prog.datenbankspiel.dto.profile;

import lombok.Data;

@Data
public class AdminUpdatePlayerDto {
    private String firstName;
    private String lastName;
    private String email;
    private Long groupId;
}
