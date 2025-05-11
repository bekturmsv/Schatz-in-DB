package com.prog.datenbankspiel.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PlayerLoginDto {
    private Long id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String nickname;
    private String name;
    private Long points;
    private String currentTheme;
    private List<String> purchasedThemes;

    private Map<String, Object> progress;
    private List<Map<String, Object>> tasks;
    private Map<String, List<Long>> completedTasks;
    private Map<String, Boolean> completedLevels;
}
