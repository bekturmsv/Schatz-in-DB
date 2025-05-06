package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;

@Entity
public class PlayerAnswer {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private Long playerId;
    private String answer;
    private Long pointsEarned;
}

