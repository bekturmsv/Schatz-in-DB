package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.TaskType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class TaskTest extends AbstractTask {

    private Long timeLimit;

    @OneToMany(mappedBy = "taskTest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestAnswer> answers;
}
