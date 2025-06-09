package pti.softwareentwicklg.SchatzInDb.model.task;

import jakarta.persistence.*;
import lombok.Data;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "task_solution")
public class TestSolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Schwierigkeit schwierigkeitsgrad;

    @Column(name = "submitted_at")
    private LocalDateTime spentTime = LocalDateTime.now();

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt = LocalDateTime.now();

    @Column(name = "user_id")
    private Long userId;

    private Boolean correct = false;

}
