package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.task.PlayerTaskAnswerDto;
import com.prog.datenbankspiel.dto.task.PlayerTestAnswerDto;
import com.prog.datenbankspiel.model.task.PlayerTaskAnswer;
import com.prog.datenbankspiel.model.task.PlayerTestAnswer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerAnwerMapper {

    PlayerTaskAnswerDto taskToDto(PlayerTaskAnswer answer);
    PlayerTaskAnswer taskFromDto(PlayerTaskAnswerDto answer);

    PlayerTestAnswerDto testToDto(PlayerTestAnswer answer);
    PlayerTestAnswer testFromDto(PlayerTestAnswerDto answer);

}
