package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;

@Entity
public class TaskFromTeacher {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private Long teacherId;
    private Long groupId;

}

