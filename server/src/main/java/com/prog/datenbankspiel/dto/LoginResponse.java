package com.prog.datenbankspiel.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Object user;
}
