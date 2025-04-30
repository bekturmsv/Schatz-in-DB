package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.CreateGroupRequest;
import com.prog.datenbankspiel.dto.GroupDto;
import com.prog.datenbankspiel.model.user.Group;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-30T22:50:25+0200",
    comments = "version: 1.6.1, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class GroupMapperImpl implements GroupMapper {

    @Override
    public GroupDto toDto(Group group) {
        if ( group == null ) {
            return null;
        }

        GroupDto groupDto = new GroupDto();

        groupDto.setId( group.getId() );
        groupDto.setName( group.getName() );
        groupDto.setCode( group.getCode() );

        return groupDto;
    }

    @Override
    public Group fromCreateRequest(CreateGroupRequest request) {
        if ( request == null ) {
            return null;
        }

        Group group = new Group();

        group.setName( request.getName() );

        return group;
    }

    @Override
    public void update(CreateGroupRequest request, Group group) {
        if ( request == null ) {
            return;
        }

        group.setName( request.getName() );
    }
}
