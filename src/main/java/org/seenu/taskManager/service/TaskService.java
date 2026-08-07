package org.seenu.taskManager.service;

import org.seenu.taskManager.dto.TaskResponseDto;
import org.seenu.taskManager.dto.UserTaskSaveRequestDto;
import org.seenu.taskManager.entity.Task;
import org.seenu.taskManager.entity.TaskUser;
import org.seenu.taskManager.repository.TaskRepository;
import org.seenu.taskManager.util.TaskUtil;
import org.seenu.taskManager.util.UserAuthUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserAuthUtil  userAuthUtil;
    private final HashMap<String,FilterService> filterServiceMap;
    TaskService(TaskRepository taskRepository, UserAuthUtil userAuthUtil,List<FilterService> filterServices) {
        this.taskRepository = taskRepository;
        this.userAuthUtil = userAuthUtil;
        this.filterServiceMap = new HashMap<>();
        for(FilterService filterService: filterServices) {
            String filterType = filterService.getFilterType();
            filterServiceMap.put(filterType,filterService);
        }
    }

    public String addNewTask(UserTaskSaveRequestDto userTaskSaveRequestDto) {
        TaskUser currentUser=userAuthUtil.getTheContextUser();
        boolean exist=taskRepository.existsByTaskUserAndDueDate(currentUser,userTaskSaveRequestDto.getDueDate());
        if(exist)
        {
            return "Task with the same due date already exists for the user.";
        }
        Task newTask=new Task();
        newTask.setTaskUser(currentUser);
        newTask.setDueDate(userTaskSaveRequestDto.getDueDate());
        newTask.setTaskName(userTaskSaveRequestDto.getTaskName());
        if(userTaskSaveRequestDto.isCompleted())newTask.setCompleted(true);
        if(userTaskSaveRequestDto.isIspriority()) newTask.setIspriority(true);
        Task savedTask=taskRepository.save(newTask);
        return "Task added successfully.";
    }
    public List<TaskResponseDto> getAllTaskByDay(LocalDateTime day) {
        TaskUser currentUser=userAuthUtil.getTheContextUser();
        LocalDateTime today=day.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime future=day.withHour(23).withMinute(59).withSecond(59);
        List<Task> myallNewtasks=taskRepository.getUserTask(currentUser.getId(),today,future);
        List<TaskResponseDto> taskResponseDtoList = TaskUtil.giveMyTask(myallNewtasks);
        return taskResponseDtoList;
    }
    public List<Integer> getDaysWithTask() {
        TaskUser currentUser=userAuthUtil.getTheContextUser();
        LocalDateTime start = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = LocalDateTime.now().withDayOfMonth(
                LocalDateTime.now().toLocalDate().lengthOfMonth()
        ).withHour(23).withMinute(59).withSecond(59);
        List<LocalDateTime> hasTask=taskRepository.findDaysWithTask(currentUser.getId(),start,end);
        return hasTask.stream().map((day)->day.getDayOfMonth()).collect(Collectors.toList());

    }

    public String deleteMyTask(Long id) {
        Task mytask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task not found."));
        TaskUser currentUser=userAuthUtil.getTheContextUser();
        if(mytask.getTaskUser().getId()!=currentUser.getId())throw new RuntimeException("you r not authorised to update this task");
        taskRepository.deleteById(id);
        return "task deleted successfully.";

    }

    public String updateMyTask(Long id, UserTaskSaveRequestDto userTaskSaveRequestDto) {
        Task mytask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task not found."));
        TaskUser currentUser=userAuthUtil.getTheContextUser();
        if(mytask.getTaskUser().getId()!=currentUser.getId())throw new RuntimeException("you r not authorised to update this task");
        mytask.setDueDate(userTaskSaveRequestDto.getDueDate());
        mytask.setTaskName(userTaskSaveRequestDto.getTaskName());
        if (userTaskSaveRequestDto.isCompleted()) mytask.setCompleted(true);
        if (userTaskSaveRequestDto.isIspriority()) mytask.setIspriority(true);
        taskRepository.save(mytask);
        return "Task updated successfully.";
    }

    public List<TaskResponseDto> getFilteredTasks(String filterType) {
       if(filterType==null)throw new RuntimeException("filter type is required");
       FilterService filterService=filterServiceMap.get(filterType);
       if(filterService==null)throw new RuntimeException("invalid filter type");
       List<Task> FilteredTasks=filterService.getMyTasksByFilter();
       List<TaskResponseDto> filteredTaskDto=TaskUtil.giveMyTask(FilteredTasks);
       return filteredTaskDto;
    }
}

