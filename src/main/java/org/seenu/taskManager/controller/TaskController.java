package org.seenu.taskManager.controller;
import jakarta.validation.Valid;
import org.seenu.taskManager.dto.TaskResponseDto;
import org.seenu.taskManager.dto.UserTaskSaveRequestDto;
import org.seenu.taskManager.service.TaskService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @PostMapping
    public ResponseEntity<String> addNewTask(@Valid @RequestBody  UserTaskSaveRequestDto userTaskSaveRequestDto)
    {
        String result=taskService.addNewTask(userTaskSaveRequestDto);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/task/{day}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByDay(@PathVariable String day)
    {
        LocalDateTime today=LocalDateTime.parse(day);
        List<TaskResponseDto> todayTasks=taskService.getAllTaskByDay(today);
        return ResponseEntity.ok(todayTasks);
    }
    @DeleteMapping("/deletetask/{id}")
    public ResponseEntity<String> deleteMyTask(@PathVariable Long id)
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
    @GetMapping("/filter")
    public ResponseEntity<List<TaskResponseDto>> getTasksByFilter(@RequestParam(required = false) List<String> filterType,
                                                                  @RequestParam String date)
    {
        List<TaskResponseDto> filteredTask=taskService.getFilteredTasks(filterType,date);
        return ResponseEntity.ok(filteredTask);
    }

}
