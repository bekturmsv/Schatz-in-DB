package pti.softwareentwicklg.SchatzInDb.model.task;

import jakarta.persistence.*;
import lombok.Data;
import pti.softwareentwicklg.SchatzInDb.model.user.User;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_solution")
public class UserSolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_code", nullable = false)
    private String taskCode;

    @Column(name = "user_sql", columnDefinition = "TEXT", nullable = false)
    private String userSql;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt = LocalDateTime.now();

    @Column(name = "user_id")
    private Long userId;

    private Boolean correct = false;

}