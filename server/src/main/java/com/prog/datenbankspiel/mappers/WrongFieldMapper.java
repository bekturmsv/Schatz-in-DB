package com.prog.datenbankspiel.mappers;


import com.prog.datenbankspiel.dto.task.WrongFieldDto;
import com.prog.datenbankspiel.model.task.WrongField;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WrongFieldMapper {
    WrongFieldDto toDto(WrongField entity);
    WrongField fromDto(WrongFieldDto dto);
}

