package com.prog.datenbankspiel.dto.task;

import lombok.Data;

@Data
public class CreateTestAnswerRequest {
    private String answerText;
    private boolean correct;
}

