package com.prog.datenbankspiel.model.task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Level {
    @Id
    @GeneratedValue
    private Long id;
}

