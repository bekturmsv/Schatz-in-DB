package pti.softwareentwicklg.SchatzInDb.model.task;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
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
