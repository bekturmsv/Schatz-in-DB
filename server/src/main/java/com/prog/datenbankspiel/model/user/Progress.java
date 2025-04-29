package com.prog.datenbankspiel.model.user;

import com.prog.datenbankspiel.model.task.AbstractTask;
import com.prog.datenbankspiel.model.task.Hint;
import com.prog.datenbankspiel.model.task.Level;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Progress {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Set<Long> completedTaskIds = new HashSet<>();

    private LevelDifficulty difficulty;

    private Long current_task;

}
