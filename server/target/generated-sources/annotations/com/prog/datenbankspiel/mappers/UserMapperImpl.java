package com.prog.datenbankspiel.mappers;

import com.prog.datenbankspiel.dto.PlayerDto;
import com.prog.datenbankspiel.dto.UserDto;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-06T16:11:36+0200",
    comments = "version: 1.6.1, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public PlayerDto toPlayerDto(Player player) {
        if ( player == null ) {
            return null;
        }

        PlayerDto.PlayerDtoBuilder<?, ?> playerDto = PlayerDto.builder();

        playerDto.points( player.getTotal_points() );
        playerDto.design( player.getDesign() );
        List<String> list = player.getPurchasedThemes();
        if ( list != null ) {
            playerDto.purchasedThemes( new ArrayList<String>( list ) );
        }
        playerDto.currentTheme( player.getCurrentTheme() );
        playerDto.id( player.getId() );
        playerDto.username( player.getUsername() );
        playerDto.email( player.getEmail() );
        playerDto.firstName( player.getFirstName() );
        playerDto.lastName( player.getLastName() );
        playerDto.role( player.getRole() );

        return playerDto.build();
    }

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder<?, ?> userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.username( user.getUsername() );
        userDto.email( user.getEmail() );
        userDto.firstName( user.getFirstName() );
        userDto.lastName( user.getLastName() );
        userDto.role( user.getRole() );

        return userDto.build();
    }
}
