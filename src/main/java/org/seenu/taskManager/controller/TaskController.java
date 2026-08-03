package org.seenu.taskManager.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.seenu.taskManager.dto.TaskResponceDto;
import org.seenu.taskManager.dto.UserTaskSaveRequestDto;
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
    @GetMapping("/task/{day}")
    public ResponseEntity<List<TaskResponceDto>> getTasksByDay(@PathVariable String day)
    {
        LocalDateTime today=LocalDateTime.parse(day);
        List<TaskResponceDto> todayTasks=taskService.getAllTaskByDay(today);
        return ResponseEntity.ok(todayTasks);
    }
    @DeleteMapping("/deletetask/{id}")
    public ResponseEntity<String> deteteMyTask(@PathVariable Long id)
    {
        String result=taskService.deleteMyTask(id);
        return ResponseEntity.ok(result);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateMyTask(@PathVariable Long id,@RequestBody UserTaskSaveRequestDto userTaskSaveRequestDto)
    {
        String result=taskService.updateMyTask(id,userTaskSaveRequestDto);
        return ResponseEntity.ok(result);
    }

}
