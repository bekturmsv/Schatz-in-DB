package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;

@Entity
public class TestQuestions {
    @Id
    @GeneratedValue
    private Long id;
    private String question;
    private String answer;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;
}
