package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.PlayerDto;
import com.prog.datenbankspiel.model.user.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mappings({
            @Mapping(target = "playerId", source = "id"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "role", source = "role"),
            @Mapping(target = "totalPoints", source = "total_points"),
            @Mapping(target = "levelId", source = "level_id"),
            @Mapping(target = "design", source = "design"),
            @Mapping(target = "specialistGroup", source = "specialist_group"),
            @Mapping(target = "matriculationNumber", source = "matriculation_number"),
            @Mapping(target = "groupId", source = "groupId.id")
    })
    PlayerDto toDto(Player player);
}
