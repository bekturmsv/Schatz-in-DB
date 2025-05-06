package com.prog.datenbankspiel.model.task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Test {
    @Id
    @GeneratedValue
    private Long id;
    private Long level_id;
    private String title;
    private String description;
    private String correct_answer;
    private Long time;
    private Long teacher_id;
}
