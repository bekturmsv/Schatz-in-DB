package com.prog.datenbankspiel.security;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}

