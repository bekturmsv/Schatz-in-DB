package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import lombok.Data;

import java.util.List;

@Data
public class TaskTestDto extends AbstractTaskDto {
    private String question;
    private Long timeLimit;
    private List<TestAnswerDto> answers;
}



