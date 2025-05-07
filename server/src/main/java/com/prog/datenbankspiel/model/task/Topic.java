package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private LevelDifficulty levelDifficulty;

    public Topic() {
    }

    public Topic(String name, LevelDifficulty levelDifficulty) {
        this.name = name;
        this.levelDifficulty = levelDifficulty;
    }

}
