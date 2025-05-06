package com.prog.datenbankspiel.model.task;

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
    private Long points;

    @ManyToOne
    @JoinColumn(name = "topics_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private TaskType taskType;
}

