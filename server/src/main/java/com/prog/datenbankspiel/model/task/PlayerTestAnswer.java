package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;
import lombok.Data;

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

    private Long result;
}
