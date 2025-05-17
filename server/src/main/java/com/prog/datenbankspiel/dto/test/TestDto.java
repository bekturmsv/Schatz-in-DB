package com.prog.datenbankspiel.dto.test;

import com.prog.datenbankspiel.dto.task.TaskDto;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TestDto {
    private Long id;
    private LevelDifficulty levelDifficulty;
    private Long pointsEarned;
    private List<TaskDto> testTaskList;
}
