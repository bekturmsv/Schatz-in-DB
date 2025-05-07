package com.prog.datenbankspiel.model.user;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Progress {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection
    private Set<Long> completedTaskIds = new HashSet<>();

    private Long currentLevelId;

}
