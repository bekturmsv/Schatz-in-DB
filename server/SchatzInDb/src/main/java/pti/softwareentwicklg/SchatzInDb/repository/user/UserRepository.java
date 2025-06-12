package pti.softwareentwicklg.SchatzInDb.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.softwareentwicklg.SchatzInDb.model.enums.Roles;
import pti.softwareentwicklg.SchatzInDb.model.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndRole(String username, Roles role);
    boolean existsByUsername(String username);
}