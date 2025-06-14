package pti.softwareentwicklg.SchatzInDb.model.task;

import jakarta.persistence.*;
import lombok.*;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskInteractionType;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;

@Data
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String taskCode;


    @Column(length = 1000)
    private String aufgabe;

    @Column(columnDefinition = "TEXT")
    private String solution;

    @Enumerated(EnumType.STRING)
    private SqlKategorie kategorie;

    @Enumerated(EnumType.STRING)
    private Schwierigkeit schwierigkeitsgrad;

    private Boolean aktiv = true;

    @Column(length = 500)
    private String hint;

    @Column(nullable = false)
    private Integer points = 1;

    @Enumerated(EnumType.STRING)
    private TaskType taskType = TaskType.REGULAR;

    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type")
    private TaskInteractionType interactionType = TaskInteractionType.SQL_INPUT;

    @OneToOne(
            mappedBy = "task",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private IncorrectSqlExample incorrectSqlExample;

    @OneToOne(
            mappedBy = "task",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private DragAndDropExample dragAndDropExample;

    public Task() {
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Task;
    }

}