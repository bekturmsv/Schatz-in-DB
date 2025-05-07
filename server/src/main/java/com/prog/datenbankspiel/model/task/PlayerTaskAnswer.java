package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class PlayerTaskAnswer {
    @Id
    @GeneratedValue
    private Long id;

    private Long playerId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private String answer;

    private Long pointsEarned;

    private LocalDateTime date;
}
