package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class TaskQuery extends Task {

    @Column(columnDefinition = "TEXT")
    private String setupQuery;

    @Column(columnDefinition = "TEXT")
    private String rightAnswer;

}

