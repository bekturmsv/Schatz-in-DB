package pti.softwareentwicklg.SchatzInDb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import pti.softwareentwicklg.SchatzInDb.model.task.TestSolution;

import java.time.LocalDate;
import java.util.List;

@Data
public class RatingRequest {

    private List<Rating> ratingForEasy;

    private List<Rating> ratingForMedium;

    private List<Rating> ratingForHard;

    @Data
    @AllArgsConstructor
    public static class Rating {
        private String username;
        private LocalDate times;
    }
}
