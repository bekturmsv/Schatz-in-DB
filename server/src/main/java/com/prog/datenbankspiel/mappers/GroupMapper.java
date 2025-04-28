package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.CreateGroupRequest;
import com.prog.datenbankspiel.dto.GroupDto;
import com.prog.datenbankspiel.model.user.Group;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupDto toDto(Group group);
    Group fromCreateRequest(CreateGroupRequest request);

    void update(CreateGroupRequest request, @MappingTarget Group group);
}
