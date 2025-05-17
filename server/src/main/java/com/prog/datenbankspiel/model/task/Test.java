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

    // Число очков выдаваемое за успешное выполнение теста
    private Long pointsEarned;

    @OneToMany(mappedBy = "test")
    private List<TestQuestions> testQuestionList;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<PlayerTestAnswer> playerTestAnswers;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Task> testTaskList;
}
