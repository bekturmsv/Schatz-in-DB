package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Hint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @OneToOne
    @JoinColumn(name = "task_id")
    private AbstractTask task;

}


