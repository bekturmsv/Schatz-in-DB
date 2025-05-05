package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.UserDto;
import com.prog.datenbankspiel.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
