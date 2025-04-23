package com.prog.datenbankspiel.model.user;

import com.prog.datenbankspiel.model.task.AbstractTask;
import com.prog.datenbankspiel.model.task.Hint;
import com.prog.datenbankspiel.model.task.Level;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Progress {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToMany
//    @JoinTable(
//            name = "progress_task",
//            joinColumns = @JoinColumn(name = "progress_id"),
//            inverseJoinColumns = @JoinColumn(name = "task_id")
//    )

    private Long points;

}
