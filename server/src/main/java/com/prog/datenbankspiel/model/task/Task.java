package com.prog.datenbankspiel.model.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.enums.TaskType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topics_id")
    private Topic topic;

    @Enumerated(EnumType.STRING)
    private LevelDifficulty levelDifficulty;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    private String title;
    private String description;
    private String taskAnswer;
    private Long points;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
    private Hint hint;

    @OneToMany(mappedBy = "task")
    private List<PlayerTaskAnswer> playerTaskAnswers;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode sampleData;

}

