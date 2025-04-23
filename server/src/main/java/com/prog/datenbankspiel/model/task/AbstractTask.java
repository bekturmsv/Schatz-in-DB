package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.user.Progress;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @Enumerated(EnumType.STRING)
    private LevelDifficulty difficulty;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
    private Hint hint;

    private Long points;

//    @ManyToMany(mappedBy = "tasks")
//    private List<Progress> progressList;


}


