package com.prog.datenbankspiel.dto.front;

import lombok.Data;

import java.util.List;

@Data
public class TaskGroupDto {
    private List<FinalTestTaskDto> regularTasks;
    private FinalTestWrapper finalTest;
}