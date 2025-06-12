package pti.softwareentwicklg.SchatzInDb.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "teacher")
public class Teacher extends User {

    private String subject;
}