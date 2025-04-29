package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TestAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answerText;

    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "task_test_id")
    private TaskTest taskTest;
}
