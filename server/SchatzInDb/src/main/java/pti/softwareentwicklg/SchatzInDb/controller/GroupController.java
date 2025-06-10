package pti.softwareentwicklg.SchatzInDb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pti.softwareentwicklg.SchatzInDb.dto.GroupDto;
import pti.softwareentwicklg.SchatzInDb.dto.request.ChangeGroupRequest;
import pti.softwareentwicklg.SchatzInDb.dto.request.CreateGroupRequest;
import pti.softwareentwicklg.SchatzInDb.dto.request.JoinGroupRequest;
import pti.softwareentwicklg.SchatzInDb.dto.response.GroupJoinResponse;
import pti.softwareentwicklg.SchatzInDb.model.user.Group;
import pti.softwareentwicklg.SchatzInDb.model.user.Player;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.user.GroupRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.PlayerRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;
import pti.softwareentwicklg.SchatzInDb.service.user.GroupService;

import java.net.URI;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;

    public GroupController(GroupRepository groupRepository, GroupService groupService, UserRepository userRepository, PlayerRepository playerRepository) {
        this.groupRepository = groupRepository;
        this.groupService = groupService;
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/createGroup")
    public ResponseEntity<Group> createGroup(
            @RequestBody CreateGroupRequest request,
            UriComponentsBuilder uriBuilder, Authentication auth) {

        Group group = groupService.createGroup(request, auth);

        URI uri = uriBuilder.path("/groups/{id}").buildAndExpand(group.getId()).toUri();
        return ResponseEntity.created(uri).body(group);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public Iterable<Group> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .toList();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable Long id) {
        Group group = groupRepository.findById(id).orElse(null);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        Set<Player> students = playerRepository.findAllByGroupId(group);

        return ResponseEntity.ok(Map.of(
                "group",     group,
                "students",  students
        ));
    }

    @PreAuthorize("hasRole('PLAYER')")
    @PostMapping("/joinGroup")
    public ResponseEntity<?> joinGroup(@RequestBody JoinGroupRequest request, Authentication authentication) {
        String code = request.getGroupCode();
        Group group = groupRepository.findByCode(code)
                .orElse(null);

        if (group == null) {
            return ResponseEntity.badRequest().body("Invalid group code");
        }

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(user instanceof Player player)) {
            return ResponseEntity.status(403).body("Only players can join groups");
        }

        player.setGroupId(group);
        playerRepository.save(player);

        return ResponseEntity.ok(new GroupJoinResponse(group.getName()));
    }

    @PutMapping("/quitGroup")
    public ResponseEntity<?> quitGroup(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Player player = (Player) user;
        if (player.getGroupId() == null) {
            return ResponseEntity.badRequest().body("Student is not in a group");
        }
        player.setGroupId(null);
        playerRepository.save(player);
        return ResponseEntity.ok("Student quit group");
    }

    @PutMapping("/group")
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<String> changeGroup(@RequestBody ChangeGroupRequest request, Authentication authentication) {
        Player player = (Player) userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findByCode(request.getGroupCode())
                .orElse(null);
        if (group == null) return ResponseEntity.badRequest().body("Group not found");

        player.setGroupId(group);
        playerRepository.save(player);

        return ResponseEntity.ok("Group changed successfully");
    }
}