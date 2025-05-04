package com.prog.datenbankspiel.dto.task;

import lombok.Data;

import java.util.List;

@Data
public class SubmitTestRequest {
    private Long taskId;
    private List<Long> selectedAnswersId;
}

