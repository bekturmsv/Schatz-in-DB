package com.prog.datenbankspiel.model.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LevelDifficulty difficulty;

    private String name;

    @OneToMany(mappedBy = "topic")
    @JsonIgnore
    private List<AbstractTask> tasks;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

}
