package com.prog.datenbankspiel.model.task;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class DragAndDropField {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private List<String> dragDrop;
}
