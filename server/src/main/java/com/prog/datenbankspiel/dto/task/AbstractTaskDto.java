package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.enums.TaskType;
import lombok.Data;

@Data
public abstract class AbstractTaskDto {
    private Long id;
    private String title;
    private String description;
    private Long points;
    private LevelDifficulty difficulty;
    private TaskType taskType;
    private Long topicId;
    private Long levelId;

    private HintDto hint;
}

