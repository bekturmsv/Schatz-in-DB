package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Test {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private LevelDifficulty levelDifficulty;

    private Long time;

    private Long pointsEarned;

    @OneToMany(mappedBy = "test")
    private List<TestQuestions> testQuestionList;
}
