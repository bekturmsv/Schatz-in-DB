package com.prog.datenbankspiel.model.task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class StudyMaterial {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private String type;
    private String file_path;
    private String video_url;
    private String external_link;
    private Long teacher_id;
}
