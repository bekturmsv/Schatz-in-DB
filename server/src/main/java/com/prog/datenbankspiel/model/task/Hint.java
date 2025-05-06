package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;

@Entity
public class Hint {
    @Id
    @GeneratedValue
    private Long id;
    private String description;

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;
}

