package pti.softwareentwicklg.SchatzInDb.model.user;

import jakarta.persistence.*;
import lombok.*;
import pti.softwareentwicklg.SchatzInDb.model.enums.SpecialistGroup;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "player")
public class Player extends User {
    private Long total_points;
    private Long level_id;
    private String design;
    @Enumerated(EnumType.STRING)
    private SpecialistGroup specialist_group;
    private int matriculation_number;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group groupId;

    @ManyToMany
    @JoinTable(
            name = "player_themes",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    private Set<Theme> purchasedThemes;
}