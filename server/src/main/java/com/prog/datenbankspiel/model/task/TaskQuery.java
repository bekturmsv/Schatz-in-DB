package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.TaskType;
import jakarta.persistence.*;


@Entity
public class TaskQuery extends AbstractTask {

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    private String title;
    private String description;
    private String setupSql;
    private String rightAnswer;
    private Long points;

}

