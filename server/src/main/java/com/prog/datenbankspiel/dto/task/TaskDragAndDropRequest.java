package com.prog.datenbankspiel.dto.task;

import lombok.Data;

import java.util.List;

@Data
public class TaskDragAndDropRequest extends AbstractTaskRequest{
    private String setupText;
    private List<String> words;
}
