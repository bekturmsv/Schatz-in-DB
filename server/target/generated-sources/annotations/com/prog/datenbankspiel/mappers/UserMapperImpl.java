package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.PlayerLoginDto;
import com.prog.datenbankspiel.dto.UserDto;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T20:29:06+0200",
    comments = "version: 1.6.1, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.username( user.getUsername() );
        userDto.email( user.getEmail() );
        userDto.firstName( user.getFirstName() );
        userDto.lastName( user.getLastName() );
        userDto.role( user.getRole() );

        return userDto.build();
    }

    @Override
    public PlayerLoginDto toPlayerLoginDto(Player player) {
        if ( player == null ) {
            return null;
        }

        PlayerLoginDto playerLoginDto = new PlayerLoginDto();

        playerLoginDto.setId( player.getId() );
        playerLoginDto.setUsername( player.getUsername() );
        playerLoginDto.setEmail( player.getEmail() );
        List<String> list = player.getPurchasedThemes();
        if ( list != null ) {
            playerLoginDto.setPurchasedThemes( new ArrayList<String>( list ) );
        }

        return playerLoginDto;
    }
}
