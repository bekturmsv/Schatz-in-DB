package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicDto {
    private String name;
    private LevelDifficulty levelDifficulty;
}
