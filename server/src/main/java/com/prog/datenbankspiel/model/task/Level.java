package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import jakarta.persistence.*;

import java.util.List;


@Entity
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LevelDifficulty difficulty;

    @OneToMany(mappedBy = "level")
    private List<TaskQuery> taskQueries;

    @OneToMany(mappedBy = "level")
    private List<TaskTest> taskTests;

    @OneToMany(mappedBy = "level")
    private List<Topic> topics;

}
