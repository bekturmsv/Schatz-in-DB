package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;

@Entity
public class TestAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private TaskTest taskTest;

    private String answer;

}