package com.prog.datenbankspiel.dto.front;

import lombok.Data;

@Data
public class FinalTestTaskDto {
    private Long id;
    private String name;
    private String topic;
    private Long points;
    private String description;
    private String hint;
    private String correctAnswer;
}
