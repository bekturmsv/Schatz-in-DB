package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class WrongField {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private String wrongField;
}
