package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.enums.TaskType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topics_id")
    private Topic topic;

    @Enumerated(EnumType.STRING)
    private LevelDifficulty levelDifficulty;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    private String title;
    private String description;
    private String taskAnswer;

    private Long points;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
    private Hint hint;

    @OneToMany(mappedBy = "task")
    private List<PlayerTaskAnswer> playerTaskAnswers;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL,
            orphanRemoval = true, optional = true)
    private TaskSampleData sampleData;

    // A handy setter so that the service doesn't have to manually link both parties
    public void setSampleData(TaskSampleData sampleData) {
        if (sampleData != null) sampleData.setTask(this);
        this.sampleData = sampleData;
    }
}

