package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.Task;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PlayerTaskAnswerDto {
    private Long playerId;
    private Task task;
    private String answer;
    private Long pointsEarned;
    private LocalDateTime date;
}
