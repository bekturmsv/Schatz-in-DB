package pti.softwareentwicklg.SchatzInDb.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.softwareentwicklg.SchatzInDb.model.user.Group;
import pti.softwareentwicklg.SchatzInDb.model.user.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findById(Long id);

    Player findPlayerById(Long id);

    List<Player> findAll();

    Set<Player> findAllByGroupId(Group groupId);
}