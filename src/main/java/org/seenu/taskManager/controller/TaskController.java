package org.seenu.taskManager.controller;
import jakarta.validation.Valid;
import org.seenu.taskManager.dto.TaskResponseDto;
import org.seenu.taskManager.dto.UserTaskSaveRequestDto;
import org.seenu.taskManager.service.TaskService;
import org.seenu.taskManager.util.TaskUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskUtil  taskUtil;
    TaskController(TaskService taskService, TaskUtil taskUtil) {

        this.taskService = taskService;
        this.taskUtil = taskUtil;
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
        LocalDate today=taskUtil.validateDate(day);
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
        LocalDate parsedDate=taskUtil.validateDate(date);
        List<TaskResponseDto> filteredTask=taskService.getFilteredTasks(filterType,parsedDate);
        return ResponseEntity.ok(filteredTask);
    }

}
