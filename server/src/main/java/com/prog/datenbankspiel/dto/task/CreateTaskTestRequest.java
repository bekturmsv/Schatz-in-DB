package com.prog.datenbankspiel.dto.task;

import lombok.Data;

import java.util.List;

@Data
public class CreateTaskTestRequest extends AbstractTaskRequest {
    private Long timeLimit;
    private List<CreateTestAnswerRequest> answers;
}



