package com.prog.datenbankspiel.repository.user;

import com.prog.datenbankspiel.model.user.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findById(Long id);

    Optional<Player> findByUsername(String username);
}
