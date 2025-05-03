package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import lombok.Data;

import java.util.List;

@Data
public class LevelDto {
    private Long id;
    private LevelDifficulty difficulty;

    private List<AbstractTaskDto> tasks;       // Optional: for combined TaskQuery/Drag
    private List<TaskTestDto> testTasks;        // Optional: for test tasks only
}



