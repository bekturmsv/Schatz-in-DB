package com.prog.datenbankspiel.model.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "themes")
@Data
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Long cost;
}
