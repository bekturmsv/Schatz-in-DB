package com.prog.datenbankspiel.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LoginResponse {
    private String token;
    private UserDto user;
    private List<Map<String, Object>> tasks;
    private Map<String, Object> progress;

}
