package pti.softwareentwicklg.SchatzInDb.model.user;

import jakarta.persistence.*;
import lombok.Data;
import pti.softwareentwicklg.SchatzInDb.model.enums.Roles;

@Data
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Roles role;

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

}