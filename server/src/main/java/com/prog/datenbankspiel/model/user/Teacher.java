package com.prog.datenbankspiel.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "teacher")
public class Teacher extends User {

    private Long subject;
}
