package com.prog.datenbankspiel.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitResponse {
    private boolean correct;
    private Long earnedPoints;
}
