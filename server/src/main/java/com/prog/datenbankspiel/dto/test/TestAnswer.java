package com.prog.datenbankspiel.dto.test;

import lombok.Data;

import java.util.Map;

@Data
public class TestAnswer {
    private Map<Long, String> answers;
    private Long timeSpent;
}

