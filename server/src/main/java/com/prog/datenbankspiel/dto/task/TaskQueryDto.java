package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import lombok.Data;

@Data
public class TaskQueryDto extends AbstractTaskDto {
    private String setupSql;
    private String rightAnswer;
}



