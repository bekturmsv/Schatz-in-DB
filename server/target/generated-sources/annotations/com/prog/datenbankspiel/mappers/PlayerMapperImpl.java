package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.PlayerDto;
import com.prog.datenbankspiel.model.user.Group;
import com.prog.datenbankspiel.model.user.Player;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T19:41:00+0200",
    comments = "version: 1.6.1, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class PlayerMapperImpl implements PlayerMapper {

    @Override
    public PlayerDto toDto(Player player) {
        if ( player == null ) {
            return null;
        }

        PlayerDto playerDto = new PlayerDto();

        playerDto.setPlayerId( player.getId() );
        playerDto.setUsername( player.getUsername() );
        playerDto.setEmail( player.getEmail() );
        playerDto.setFirstName( player.getFirstName() );
        playerDto.setLastName( player.getLastName() );
        if ( player.getRole() != null ) {
            playerDto.setRole( player.getRole().name() );
        }
        playerDto.setTotalPoints( player.getTotal_points() );
        playerDto.setLevelId( player.getLevel_id() );
        playerDto.setDesign( player.getDesign() );
        playerDto.setSpecialistGroup( player.getSpecialist_group() );
        playerDto.setMatriculationNumber( player.getMatriculation_number() );
        playerDto.setGroupId( playerGroupIdId( player ) );

        return playerDto;
    }

    private Long playerGroupIdId(Player player) {
        Group groupId = player.getGroupId();
        if ( groupId == null ) {
            return null;
        }
        return groupId.getId();
    }
}
