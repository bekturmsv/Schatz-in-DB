package com.prog.datenbankspiel.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.prog.datenbankspiel.dto.task.TaskDto;
import com.prog.datenbankspiel.dto.test.TestAnswer;
import com.prog.datenbankspiel.dto.test.TestDto;
import com.prog.datenbankspiel.mappers.TestMapper;
import com.prog.datenbankspiel.model.task.*;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.repository.task.*;
import com.prog.datenbankspiel.repository.user.UserRepository;
import com.prog.datenbankspiel.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;
    private final TestMapper testMapper;
    private final PlayerTestAnswerRepository playerTestAnswerRepository;
    private final TestRepository testRepository;
    private final HintRepository hintRepository;
    private final TopicRepository topicRepository;
    private final TaskRepository taskRepository;
    private final TestService testService;

    //  PLAYER ENDPOINTS

    @Transactional
    @PostMapping("/submit/{difficulty}")
    public ResponseEntity<?> submitTestAnswer(
            @PathVariable String difficulty,
            @RequestBody TestAnswer request,
            Authentication authentication) {

        Map<String, Object> response = testService.submitTestAnswer(difficulty, request, authentication);
        return ResponseEntity.ok(response);
    }

    @Transactional
    @GetMapping("/{difficulty}")
    public MappingJacksonValue getTest(@PathVariable String difficulty) {
        LevelDifficulty level = LevelDifficulty.valueOf(difficulty.toUpperCase());
        TestDto testDto = testMapper.toDto(testService.getTestByDifficulty(level));

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .serializeAllExcept("taskAnswer"); // <== HIDE THIS FIELD

        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("taskDtoFilter", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(testDto);
        mapping.setFilters(filters);
        return mapping;
    }


    //  ADMIN ENDPOINTS

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public MappingJacksonValue createTestWithTasks(@RequestBody TestDto dto) {
        Test test = testService.createTest(dto);
        TestDto result = testMapper.toDto(test);

        // Apply filter to hide 'taskAnswer' in response
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("taskAnswer");
        FilterProvider filters = new SimpleFilterProvider().addFilter("taskDtoFilter", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(result);
        mapping.setFilters(filters);
        return mapping;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{difficulty}/task/add")
    public ResponseEntity<TestDto> addTaskToTest(
            @PathVariable String difficulty,
            @RequestBody TaskDto dto) {
        dto.setLevelDifficulty(LevelDifficulty.valueOf(difficulty.toUpperCase()));
        Task task = testService.addTaskToTest(dto);
        return ResponseEntity.ok(testMapper.toDto(task.getTest()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{difficulty}")
    public ResponseEntity<?> deleteTestByDifficulty(@PathVariable String difficulty) {
        LevelDifficulty level = LevelDifficulty.valueOf(difficulty.toUpperCase());
        testService.deleteTestByDifficulty(level);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "🗑️ Test with difficulty '" + level + "' deleted."
        ));
    }



}
