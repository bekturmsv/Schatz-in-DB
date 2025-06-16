package pti.softwareentwicklg.SchatzInDb.model.user;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(length = 6)
    private String code;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}