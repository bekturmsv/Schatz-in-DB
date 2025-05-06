package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;

@Entity
public class Topic {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "levels_id")
    private Level level;
}
