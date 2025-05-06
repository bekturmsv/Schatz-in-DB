package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class TaskTest extends Task {

    private Long timeLimit;

    @OneToMany(mappedBy = "taskTest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestAnswer> answers;
}
