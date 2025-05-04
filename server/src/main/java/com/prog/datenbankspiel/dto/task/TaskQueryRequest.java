package com.prog.datenbankspiel.dto.task;

import lombok.Data;

@Data
public class TaskQueryRequest extends AbstractTaskRequest {
    private String setupQuery;
}
