package pti.softwareentwicklg.SchatzInDb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pti.softwareentwicklg.SchatzInDb.dto.request.RatingRequest;
import pti.softwareentwicklg.SchatzInDb.service.task.TestService;

@RestController
@RequestMapping("/api/rating")
public class RatingController {

    private final TestService testService;

    public RatingController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public ResponseEntity<?> getRating() {
        RatingRequest response = testService.getRating();
        return ResponseEntity.ok(response);
    }
}
