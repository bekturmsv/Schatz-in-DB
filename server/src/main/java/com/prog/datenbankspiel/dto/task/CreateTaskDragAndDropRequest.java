package com.prog.datenbankspiel.dto.task;

import lombok.Data;

import java.util.List;

@Data
public class CreateTaskDragAndDropRequest extends AbstractTaskRequest {
    private String setupText;
    private String correctText;
    private List<String> words;
}


