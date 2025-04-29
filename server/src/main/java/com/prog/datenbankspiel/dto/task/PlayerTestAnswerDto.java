package com.prog.datenbankspiel.dto.task;

import lombok.Data;

import java.util.List;

@Data
public class PlayerTestAnswerDto {
    private Long taskId;
    private List<String> selectedAnswers;
}

