package pti.softwareentwicklg.SchatzInDb.model.task;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@Entity
@Table(name = "drag_and_drop_example")
public class DragAndDropExample {

    @Id
    @Column(name = "task_id")
    private Long taskId;

    @OneToOne
    @MapsId
    @JoinColumn(name="task_id")
    private Task task;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "drag_and_drop_query", columnDefinition = "jsonb", nullable = false)
    private List<String> dragAndDropQuery;

    public DragAndDropExample() {}
}
