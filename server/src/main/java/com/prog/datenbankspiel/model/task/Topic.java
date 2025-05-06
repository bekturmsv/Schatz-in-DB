package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import jakarta.persistence.*;

@Entity
public class Topic {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private LevelDifficulty levelDifficulty;
}
