package com.prog.datenbankspiel.dto;

import com.prog.datenbankspiel.model.user.enums.Roles;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Roles role;
}
