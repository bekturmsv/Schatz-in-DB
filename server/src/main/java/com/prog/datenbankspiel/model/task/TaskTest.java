package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class TaskTest extends AbstractTask {

    private Long teacherId;
    private String title;
    private String description;

    @OneToMany(mappedBy = "taskTest", cascade = CascadeType.ALL)
    private List<TestAnswer> answers;

    @OneToOne
    @JoinColumn(name = "correct_answer_id")
    private TestAnswer correctAnswer;

    private Long time;

}