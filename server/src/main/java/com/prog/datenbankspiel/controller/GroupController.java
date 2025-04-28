package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.CreateGroupRequest;
import com.prog.datenbankspiel.dto.GroupDto;
import com.prog.datenbankspiel.mappers.GroupMapper;
import com.prog.datenbankspiel.model.user.Group;
import com.prog.datenbankspiel.repository.user.GroupRepository;
import com.prog.datenbankspiel.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupMapper groupMapper;
    private final GroupRepository groupRepository;
    private final GroupService groupService;


    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/createGroup")
    public ResponseEntity<GroupDto> createGroup(
            @RequestBody CreateGroupRequest request,
            UriComponentsBuilder uriBuilder) {

        var groupDto = groupService.createGroup(request);

        var uri = uriBuilder.path("/teachers/{id}").buildAndExpand(groupDto.getId()).toUri();
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
}
