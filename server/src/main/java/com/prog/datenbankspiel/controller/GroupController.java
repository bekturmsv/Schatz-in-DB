package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.CreateGroupRequest;
import com.prog.datenbankspiel.dto.GroupDto;
import com.prog.datenbankspiel.mappers.GroupMapper;
import com.prog.datenbankspiel.repository.user.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupMapper groupMapper;
    private final GroupRepository groupRepository;

    // TODO CRUD classrooms

    @PostMapping("/createGroup")
    public ResponseEntity<GroupDto> createGroup(
            @RequestBody CreateGroupRequest request,
            UriComponentsBuilder uriBuilder) {

        var group = groupMapper.fromDto(request);
        groupRepository.save(group);

        var groupDto = groupMapper.toDto(group);

        var uri = uriBuilder.path("/teachers/{id}").buildAndExpand(groupDto.getId()).toUri();
        return ResponseEntity.created(uri).body(groupDto);
    }
}
