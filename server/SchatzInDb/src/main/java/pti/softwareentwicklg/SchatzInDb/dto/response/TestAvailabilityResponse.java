package pti.softwareentwicklg.SchatzInDb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestAvailabilityResponse {

    private int totalCompletedTasks;
    private int totalTasks;
    private List<LevelStatus> levels;

    @Data
    @AllArgsConstructor
    public static class LevelStatus {
        private String title;
        private boolean completed;
    }
}