package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Test extends AbstractTask {

    private Long teacherId;
    private String title;
    private String description;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<TestAnswer> answers;

    @OneToOne
    @JoinColumn(name = "correct_answer_id")
    private TestAnswer correctAnswer;

    private Long time;

}