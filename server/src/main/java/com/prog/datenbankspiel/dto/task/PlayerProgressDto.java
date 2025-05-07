package com.prog.datenbankspiel.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerProgressDto {
    private String username;
    private Long totalPoints;
}
