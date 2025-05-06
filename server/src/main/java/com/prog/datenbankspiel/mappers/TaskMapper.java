package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.task.TaskDto;
import com.prog.datenbankspiel.model.task.AbstractTask;
import com.prog.datenbankspiel.model.task.Hint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "hint", target = "hint")
    TaskDto toDto(AbstractTask task);

    default String map(Hint hint) {
        return hint != null ? hint.getText() : null;
    }

}
