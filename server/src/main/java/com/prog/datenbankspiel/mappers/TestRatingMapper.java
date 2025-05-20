package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.test.TestRatingDto;
import com.prog.datenbankspiel.model.task.PlayerTestAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TestRatingMapper {

    @Mapping(target = "playerId", source = "playerId")
    @Mapping(target = "timeSpent", source = "timeSpent")
    @Mapping(target = "pointsEarned", source = "pointsEarned")
    @Mapping(target = "testId", source = "test.id")
    @Mapping(target = "levelDifficulty", source = "test.levelDifficulty")
    TestRatingDto toDto(PlayerTestAnswer answer);
}

