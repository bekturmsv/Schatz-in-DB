package pti.softwareentwicklg.SchatzInDb.model.task;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import lombok.Data;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;

@Data
@Entity
public class StudyMaterial {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    private SqlKategorie sqlKategorie;

    @Column(name = "teacher_id")
    private Long teacher;

}