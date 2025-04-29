package com.prog.datenbankspiel.dto.task;

import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import lombok.Data;

import java.util.List;

@Data
public class TaskDragAndDropDto {

    private String title;
    private String description;
    private Long points;
    private LevelDifficulty difficulty;
    private Long topicId;
    private Long levelId;

    private String setupText;

    private String correctText;

    private List<String> words;

}

