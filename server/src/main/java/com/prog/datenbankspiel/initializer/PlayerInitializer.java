package com.prog.datenbankspiel.initializer;

import com.prog.datenbankspiel.model.user.Group;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.Progress;
import com.prog.datenbankspiel.model.user.User;
import com.prog.datenbankspiel.model.user.enums.Roles;
import com.prog.datenbankspiel.repository.user.GroupRepository;
import com.prog.datenbankspiel.repository.user.ProgressRepository;
import com.prog.datenbankspiel.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class PlayerInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProgressRepository progressRepository;
    private final EntityManager entityManager;

    @PostConstruct
    public void init() {
        for (int i = 1; i <= 5; i++) {
            String username = "player" + i;

            if (userRepository.findByUsername(username).isEmpty()) {
                Player player = new Player();
                player.setUsername(username);
                player.setPassword(passwordEncoder.encode("pass" + i));
                player.setEmail(username + "@example.com");
                player.setFirstName("Player");
                player.setLastName("No" + i);
                player.setRole(Roles.PLAYER);
                player.setTotal_points(0L);
                player.setLevel_id(1L);
                player.setDesign("default");
                player.setTotal_points(0L);

                Progress progress = new Progress();
                progress.setUser(player);
                progress.setCompletedTaskIds(new HashSet<>());
                progress.setCurrentLevelId(1L);
                player.setProgress(progress);

                userRepository.save(player);
            }
        }
    }
}
