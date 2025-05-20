package com.prog.datenbankspiel.dto.test;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestRatingDto {
    private String username;
    private Long playerId;
    private Long timeSpent;
    private Long pointsEarned;
    private Long testId;
    private LevelDifficulty levelDifficulty;
}

