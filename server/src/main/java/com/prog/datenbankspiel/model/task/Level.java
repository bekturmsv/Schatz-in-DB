package com.prog.datenbankspiel.model.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Data
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LevelDifficulty difficulty;

    @OneToMany(mappedBy = "level")
    @JsonIgnore
    private List<TaskQuery> taskQueries;

    @OneToMany(mappedBy = "level")
    @JsonIgnore
    private List<TaskTest> taskTests;

    @OneToMany(mappedBy = "level")
    @JsonIgnore
    private List<Topic> topics;

}
