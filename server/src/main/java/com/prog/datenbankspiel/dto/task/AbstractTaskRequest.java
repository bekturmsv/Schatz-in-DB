package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.enums.TaskPosition;
import com.prog.datenbankspiel.model.task.enums.TaskType;
import lombok.Data;

@Data
public abstract class AbstractTaskRequest {
    private Long id;
    private String title;
    private String description;
    private Long points;
    private LevelDifficulty difficulty;
    private TaskType taskType;
    private TaskPosition taskPosition = TaskPosition.REGULAR;
    private Long topicId;
    private String topicName;
    private Long levelId;
    private boolean finished;

    private String hint;
}

