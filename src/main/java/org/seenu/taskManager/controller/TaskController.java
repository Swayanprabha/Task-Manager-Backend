package org.seenu.taskManager.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.seenu.taskManager.dto.TaskResponceDto;
import org.seenu.taskManager.dto.UserTaskSaveRequestDto;

import org.seenu.taskManager.entity.Task;
import org.seenu.taskManager.service.TaskService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    TaskService taskService;
    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @PostMapping
    public ResponseEntity<String> addNewTask(@Valid @RequestBody  UserTaskSaveRequestDto userTaskSaveRequestDto, HttpServletRequest request)
    {
        String result=taskService.addNewTask(userTaskSaveRequestDto);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/pasttask")
    public ResponseEntity<List<TaskResponceDto>> getAllPastTasks()
    {
        List<TaskResponceDto> allOldTasks=taskService.getAllOldTasks();
        return ResponseEntity.ok(allOldTasks);
    }
    @GetMapping("/futuretask")
    public ResponseEntity<List<TaskResponceDto>> getAllFutureTasks()
    {
        List<TaskResponceDto> allNewTasks=taskService.getAllNewTasks();
        return ResponseEntity.ok(allNewTasks);
    }
    @GetMapping("/task/{day}")
    public ResponseEntity<List<TaskResponceDto>> getTasksByDay(@PathVariable LocalDateTime day)
    {
        List<TaskResponceDto> todayTasks=taskService.getAllTaskByDay(day);
        return ResponseEntity.ok(todayTasks);
    }
}
