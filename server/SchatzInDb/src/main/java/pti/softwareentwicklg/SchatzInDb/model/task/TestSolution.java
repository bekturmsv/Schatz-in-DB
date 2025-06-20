package pti.softwareentwicklg.SchatzInDb.model.task;

import jakarta.persistence.*;
import lombok.Data;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "test_solution")
public class TestSolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Schwierigkeit schwierigkeitsgrad;

    @Column(name = "spent_time")
    private int spentTimeInSeconds;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt = LocalDateTime.now();

    @Column(name = "user_id")
    private Long userId;

    private Boolean correct = false;

}
