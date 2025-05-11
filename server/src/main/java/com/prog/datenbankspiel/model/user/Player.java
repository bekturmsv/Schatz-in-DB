package com.prog.datenbankspiel.model.user;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
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


}
