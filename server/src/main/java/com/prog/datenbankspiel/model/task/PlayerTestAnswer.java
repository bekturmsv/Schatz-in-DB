package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class PlayerTestAnswer {
    @Id
    @GeneratedValue
    private Long id;

    private Long playerId;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    private Long pointsEarned;

    // Время потраченное на тест (в минутах)
    private Long timeSpent;

    private LocalDateTime date;
}
