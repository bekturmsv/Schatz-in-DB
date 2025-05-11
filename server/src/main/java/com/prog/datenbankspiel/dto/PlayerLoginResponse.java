package com.prog.datenbankspiel.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PlayerLoginResponse {
    private String token;
    private PlayerLoginDto user;
}
