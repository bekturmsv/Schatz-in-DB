package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import lombok.Data;

@Data
public class TaskQueryDto {

    private String title;
    private String description;
    private Long points;
    private LevelDifficulty difficulty;
    private Long topicId;
    private Long levelId;

    private String setupSql;
    private String rightAnswer;
}


