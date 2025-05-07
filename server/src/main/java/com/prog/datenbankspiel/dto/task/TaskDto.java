package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.enums.TaskType;
import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String taskAnswer;
    private String hint;
    private Long points;
    private Long topicId;
    private String topicName;
    private LevelDifficulty levelDifficulty;
    private TaskType taskType;
}
