package pti.softwareentwicklg.SchatzInDb.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
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
        private int spentTimeInSeconds;
    }
}
