package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.PlayerDto;
import com.prog.datenbankspiel.dto.UserDto;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "points", source = "total_points")
    @Mapping(target = "design", source = "design")
    @Mapping(target = "purchasedThemes", source = "purchasedThemes")
    @Mapping(target = "currentTheme", source = "currentTheme")
    PlayerDto toPlayerDto(Player player);

    UserDto toDto(User user);
}
