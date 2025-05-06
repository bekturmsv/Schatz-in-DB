package com.prog.datenbankspiel.model.task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class TaskFromTeacher {
    @Id
    @GeneratedValue
    private Long id;
    private Long topics_id;
    private Long level_id;
    private Long task_types_id;
    private String title;
    private String description;
    private String task_answer;
    private Long points;
}
