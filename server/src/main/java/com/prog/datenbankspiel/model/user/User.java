package com.prog.datenbankspiel.model.user;

import com.prog.datenbankspiel.model.task.AbstractTask;
import com.prog.datenbankspiel.model.user.enums.Roles;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Enumerated(EnumType.STRING)
    public Roles role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Progress progress;

    public String username;

    public String password;

    public String email;

    public String first_name;

    public String last_name;

}
