package com.prog.datenbankspiel.mappers;


import com.prog.datenbankspiel.dto.task.DragAndDropFieldDto;
import com.prog.datenbankspiel.model.task.DragAndDropField;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DragAndDropFieldMapper {
    DragAndDropFieldDto toDto(DragAndDropField entity);
    DragAndDropField fromDto(DragAndDropFieldDto dto);
}
