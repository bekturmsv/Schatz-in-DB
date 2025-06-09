package pti.softwareentwicklg.SchatzInDb.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pti.softwareentwicklg.SchatzInDb.dto.request.AuthRequest;
import pti.softwareentwicklg.SchatzInDb.dto.request.RegisterRequest;
import pti.softwareentwicklg.SchatzInDb.dto.response.LoginResponse;
import pti.softwareentwicklg.SchatzInDb.dto.response.PlayerRegisterResponse;
import pti.softwareentwicklg.SchatzInDb.model.enums.Roles;
import pti.softwareentwicklg.SchatzInDb.model.user.Player;
import pti.softwareentwicklg.SchatzInDb.model.user.Theme;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.user.ThemeRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;
import pti.softwareentwicklg.SchatzInDb.service.JwtService;
import pti.softwareentwicklg.SchatzInDb.service.UserDetailsServiceImpl;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final ThemeRepository themeRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String jwt = jwtService.generateToken(userDetails);

        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setUser(user);
        return ResponseEntity.ok(response);
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        Player player = new Player();
        player.setUsername(request.getUsername());
        player.setPassword(passwordEncoder.encode(request.getPassword()));
        player.setEmail(request.getEmail());
        player.setFirstName(request.getFirstName());
        player.setLastName(request.getLastName());
        player.setRole(Roles.PLAYER);
        player.setTotal_points(0L);
        player.setLevel_id(1L);
        player.setDesign("default");
        Theme defaultTheme = themeRepository.findByName("default")
                .orElseThrow(() -> new RuntimeException("Default theme not found"));
        player.setPurchasedThemes(Set.of(defaultTheme));

        player.setMatriculation_number(request.getMatriculationNumber());
        player.setSpecialist_group(request.getSpecialistGroup());

        userRepository.save(player);

        PlayerRegisterResponse dto = new PlayerRegisterResponse();
        dto.setId(player.getId());
        dto.setUsername(player.getUsername());
        dto.setEmail(player.getEmail());
        dto.setFirstName(player.getFirstName());
        dto.setLastName(player.getLastName());
        dto.setNickname(player.getUsername());
        dto.setRole(player.getRole().name());

        return ResponseEntity.ok(dto);
    }
}