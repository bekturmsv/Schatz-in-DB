package com.prog.datenbankspiel.dto.task;

import lombok.Data;

@Data
public class SubmitQueryRequest {
    private Long taskId;
    private String queryAnswer;
}

