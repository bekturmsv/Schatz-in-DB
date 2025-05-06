package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.enums.TaskType;
import jakarta.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String description;
    private String taskAnswer;
    private String correctAnswer;
    private Long points;
    private Long time;
    private Long teacherId;
    private Long groupId;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    private LevelDifficulty levelDifficulty;

    @ManyToOne
    @JoinColumn(name = "topics_id")
    private Topic topic;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
    private Hint hint;
}

