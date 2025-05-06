package com.prog.datenbankspiel.model.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "player")
public class Player extends User {
    private Long total_points;
    private Long level_id;
    private String design;
    private String specialist_group;
    private int matriculation_number;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group groupId;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Progress progress;
    @ElementCollection
    private List<String> purchasedThemes;

    private String currentTheme;

}
