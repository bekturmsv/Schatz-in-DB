package pti.softwareentwicklg.SchatzInDb.model.task;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "zeuge")
public class Zeuge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    private Integer alter;

    @Column(length = 100)
    private String wohnort;
}