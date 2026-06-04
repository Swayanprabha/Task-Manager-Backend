package org.seenu.taskManager.controller;

import org.seenu.taskManager.entity.TaskUser;
import org.seenu.taskManager.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {
    private TaskService taskService;
    HomeController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping
    public String home() {
        TaskUser currentUser = (TaskUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return currentUser.getName();
    }
    @GetMapping("/taskdays")
    public ResponseEntity<List<Integer>> daysWithTask() {
        List<Integer> daysWithTask = taskService.getDaysWithTask();
        return ResponseEntity.ok(daysWithTask);
    }

}

