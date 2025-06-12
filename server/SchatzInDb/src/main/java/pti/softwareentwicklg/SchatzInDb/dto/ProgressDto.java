package pti.softwareentwicklg.SchatzInDb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressDto {
    private int tasksSolved;
    private int totalTasks;
}