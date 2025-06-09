package pti.softwareentwicklg.SchatzInDb.model.task;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "verdaechtiger")
public class Verdaechtiger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    private Integer alter;

    @Column(length = 100)
    private String wohnort;

    @Column(length = 50)
    private String haarfarbe;

    private Integer schuhgroesse;

    private Integer groesse;

    @Column(length = 1)
    private String geschlecht;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer vorstrafen;
}