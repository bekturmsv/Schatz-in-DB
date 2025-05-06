package com.prog.datenbankspiel.model.task;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;


@Entity
@Data
public class TaskDragAndDrop extends Task {

    private String setupText;

    private String correctText;

    private List<String> words;
}
