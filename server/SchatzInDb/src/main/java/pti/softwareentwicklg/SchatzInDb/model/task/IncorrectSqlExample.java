package pti.softwareentwicklg.SchatzInDb.model.task;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "incorrect_sql_example")
public class IncorrectSqlExample {

    @Id
    @Column(name = "task_id")
    private Long taskId;

    @OneToOne
    @MapsId
    @JoinColumn(name="task_id")
    private Task task;

    @Column(name = "wrong_query", columnDefinition = "TEXT", nullable = false)
    private String wrongQuery;

    public IncorrectSqlExample() {}
}
