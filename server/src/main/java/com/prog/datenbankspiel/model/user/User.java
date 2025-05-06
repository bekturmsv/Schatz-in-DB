package com.prog.datenbankspiel.model.user;

import com.prog.datenbankspiel.model.user.enums.Roles;
import jakarta.persistence.*;
import lombok.Data;

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
