package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.test.TestDto;
import com.prog.datenbankspiel.model.task.Test;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface TestMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "levelDifficulty", source = "levelDifficulty"),
            @Mapping(target = "pointsEarned", source = "pointsEarned"),
            @Mapping(target = "testTaskList", source = "testTaskList") // <-- explicit now
    })
    TestDto toDto(Test test);

    @Mappings({
            @Mapping(target = "levelDifficulty", source = "levelDifficulty"),
            @Mapping(target = "pointsEarned", source = "pointsEarned"),
            @Mapping(target = "testTaskList", source = "testTaskList"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "testQuestionList", ignore = true)
    })
    Test fromDto(TestDto dto);

    @Mappings({
            @Mapping(target = "levelDifficulty", source = "levelDifficulty"),
            @Mapping(target = "pointsEarned", source = "pointsEarned"),
            @Mapping(target = "testTaskList", source = "testTaskList"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "testQuestionList", ignore = true)
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTestFromDto(TestDto dto, @MappingTarget Test test);
}

