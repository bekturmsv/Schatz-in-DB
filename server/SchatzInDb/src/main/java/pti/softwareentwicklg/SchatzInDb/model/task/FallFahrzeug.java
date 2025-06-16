package pti.softwareentwicklg.SchatzInDb.model.task;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "fall_fahrzeug")
@IdClass(FallFahrzeugId.class)
public class FallFahrzeug {

    @Id
    private Integer fallId;

    @Id
    private Integer fahrzeugId;
}