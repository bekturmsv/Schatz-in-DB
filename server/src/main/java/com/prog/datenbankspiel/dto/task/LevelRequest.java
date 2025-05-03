package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import lombok.Data;

import java.util.List;

@Data
public class LevelRequest {
    private Long id;
    private LevelDifficulty difficulty;

    private List<AbstractTaskRequest> tasks;
    private List<CreateTaskTestRequest> testTasks;
}



