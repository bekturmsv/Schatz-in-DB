package com.prog.datenbankspiel.model.task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class PlayerAnswer {
    @Id
    @GeneratedValue
    private Long id;
    private Long player_id;
    private Long task_id;
    private String answer;
    private Long points_earned;
}
