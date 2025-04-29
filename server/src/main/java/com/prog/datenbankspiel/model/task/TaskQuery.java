package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.TaskType;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class TaskQuery extends AbstractTask {

    private String setupSql;

    private String rightAnswer;

}

