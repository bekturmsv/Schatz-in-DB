package com.prog.datenbankspiel.dto.task;

import java.util.List;

public class TaskTestRequest extends AbstractTaskRequest{
    private String question;
    private Long timeLimit;
    private List<TestAnswerRequest> answers;
}
