package com.prog.datenbankspiel.dto.task;

import lombok.Data;

@Data
public class CreateTaskQueryRequest extends AbstractTaskRequest {
    private String setupSql;
    private String rightAnswer;
}



