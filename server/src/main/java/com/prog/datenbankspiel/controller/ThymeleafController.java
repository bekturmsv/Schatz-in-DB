package com.prog.datenbankspiel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//Just For Testing
@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/user/auth")
    public String loginPage() {
        return "user/auth";
    }

    @GetMapping("/user/register")
    public String registerPage() {
        return "user/register";
    }

    @GetMapping("/user/data")
    public String userPage() {
        return "user/user";
    }

    @GetMapping("/task/create")
    public String createTaskPage() {
        return "task/create-task-query";
    }

    @GetMapping("/task/test/create")
    public String createTestPage() {
        return "task/create-task-test";
    }

    @GetMapping("task/submit-query")
    public String showSubmitTaskQueryPage(@RequestParam Long id, Model model) {
        model.addAttribute("taskId", id);
        return "task/submit-task-query";
    }

    @GetMapping("task/submit-test")
    public String showSubmitTaskTestPage(@RequestParam Long id, Model model) {
        model.addAttribute("taskId", id);
        return "task/submit-task-test";
    }

    @GetMapping("/levels")
    public String allLevelsPage() {
        return "level/all-levels";
    }

    @GetMapping("/level")
    public String levelPage() {
        return "level/level";
    }
}
