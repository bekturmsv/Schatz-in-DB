package com.prog.datenbankspiel.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto extends UserDto {
    private Long points;
    private String design;
    private List<String> purchasedThemes;
    private String currentTheme;
}

