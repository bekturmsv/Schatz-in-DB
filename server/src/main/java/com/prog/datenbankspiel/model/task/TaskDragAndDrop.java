package com.prog.datenbankspiel.model.task;

import com.prog.datenbankspiel.model.task.enums.TaskType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.List;


@Entity
@Data
public class TaskDragAndDrop extends AbstractTask {

    private String setupText;

    private String correctText;

    @ElementCollection
    private List<String> words;
}
