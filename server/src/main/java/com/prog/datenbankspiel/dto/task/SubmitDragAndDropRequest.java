package com.prog.datenbankspiel.dto.task;

import lombok.Data;

@Data
public class SubmitDragAndDropRequest {
    private Long taskId;
    private String dragAndDropAnswer;
}

