package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.Test;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerTestAnswerDto {
    private Long playerId;
    private Test test;
    private Long result;
}
