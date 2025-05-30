package com.prog.datenbankspiel.model.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class StudyMaterial {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private String type;
    private String filePath;
    private String videoUrl;
    private String externalLink;
    private Long teacherId;
    @Column(columnDefinition = "TEXT")
    private String content;
}
