package com.prog.datenbankspiel.dto.front;

import lombok.Data;

import java.util.Map;

@Data
public class AllLevelTasksDto {
    private Map<String, TaskGroupDto> tasks;
}
