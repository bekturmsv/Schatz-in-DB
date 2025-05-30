package com.prog.datenbankspiel.repository.user;

import com.prog.datenbankspiel.model.user.Group;
import com.prog.datenbankspiel.model.user.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findById(Long id);

    Player findPlayerById(Long id);

    List<Player> findAll();

    Set<Player> findAllByGroupId(Group groupId);
}
