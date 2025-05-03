package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import lombok.Data;

import java.util.List;

@Data
public class TaskDragAndDropDto extends AbstractTaskDto {
    private String setupText;
    private String correctText;
    private List<String> words;
}


