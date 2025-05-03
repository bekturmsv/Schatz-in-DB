package com.prog.datenbankspiel.security;

import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.Progress;
import com.prog.datenbankspiel.model.user.User;
import com.prog.datenbankspiel.model.user.enums.Roles;
import com.prog.datenbankspiel.repository.user.ProgressRepository;
import com.prog.datenbankspiel.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ProgressRepository progressRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest.getRole().equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(403).body("Forbidden: Cannot register as ADMIN");
        }

        Roles role = Roles.valueOf(registerRequest.getRole().toUpperCase());
        User newUser;

        if (role == Roles.PLAYER) {
            Player player = new Player();
            player.setUsername(registerRequest.getUsername());
            player.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            player.setEmail(registerRequest.getEmail());
            player.setFirstName(registerRequest.getFirstName());
            player.setLastName(registerRequest.getLastName());
            player.setRole(Roles.PLAYER);
            player.setTotal_points(0L);
            player.setLevel_id(1L);
            player.setDesign("default");

            Progress progress = new Progress();
            progress.setUser(player);
            progress.setCurrentLevelId(1L);
            progress.setCompletedTaskIds(new HashSet<>());
            player.setProgress(progress);

            newUser = player;
        } else {
            newUser = new User();
        }
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setEmail(registerRequest.getEmail());
        newUser.setFirstName(registerRequest.getFirstName());
        newUser.setLastName(registerRequest.getLastName());

        newUser.setRole(Roles.valueOf(registerRequest.getRole().toUpperCase()));

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }

}

