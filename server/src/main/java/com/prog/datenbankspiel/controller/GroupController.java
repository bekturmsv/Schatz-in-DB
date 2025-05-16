package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.CreateGroupRequest;
import com.prog.datenbankspiel.dto.GroupDto;
import com.prog.datenbankspiel.dto.GroupJoinResponse;
import com.prog.datenbankspiel.dto.JoinGroupRequest;
import com.prog.datenbankspiel.mappers.GroupMapper;
import com.prog.datenbankspiel.model.user.Group;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.User;
import com.prog.datenbankspiel.repository.user.GroupRepository;
import com.prog.datenbankspiel.repository.user.PlayerRepository;
import com.prog.datenbankspiel.repository.user.UserRepository;
import com.prog.datenbankspiel.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupMapper groupMapper;
    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;


    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/createGroup")
    public ResponseEntity<GroupDto> createGroup(
            @RequestBody CreateGroupRequest request,
            UriComponentsBuilder uriBuilder, Authentication auth) {

        GroupDto groupDto = groupService.createGroup(request, auth);

        URI uri = uriBuilder.path("/groups/{id}").buildAndExpand(groupDto.getId()).toUri();
        return ResponseEntity.created(uri).body(groupDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Iterable<GroupDto> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .map(groupMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable Long id) {
        Group group = groupRepository.findById(id).orElse(null);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groupMapper.toDto(group));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<GroupDto> updateGroup(@PathVariable Long id, @RequestBody CreateGroupRequest request) {
        Group group = groupRepository.findById(id).orElse(null);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        groupMapper.update(request, group);
        groupRepository.save(group);

        return ResponseEntity.ok(groupMapper.toDto(group));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        Group group = groupRepository.findById(id).orElse(null);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        groupRepository.delete(group);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('PLAYER')")
    @PostMapping("/join")
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
}
