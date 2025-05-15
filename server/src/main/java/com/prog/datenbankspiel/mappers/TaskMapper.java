package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.task.PlayerTaskAnswerDto;
import com.prog.datenbankspiel.dto.task.PlayerTestAnswerDto;
import com.prog.datenbankspiel.dto.task.TaskDto;
import com.prog.datenbankspiel.model.task.PlayerTaskAnswer;
import com.prog.datenbankspiel.model.task.PlayerTestAnswer;
import com.prog.datenbankspiel.model.task.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "topicId", source = "topic.id")
    @Mapping(target = "topicName", source = "topic.name")
    @Mapping(target = "hint", source = "hint.description")
    TaskDto toDto(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hint", ignore = true)
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "playerTaskAnswers", ignore = true)
    Task fromDto(TaskDto dto);

    @Mapping(target = "hint", ignore = true)
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "playerTaskAnswers", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDto(TaskDto dto, @MappingTarget Task task);

}
