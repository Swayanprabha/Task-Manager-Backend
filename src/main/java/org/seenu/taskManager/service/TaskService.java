package org.seenu.taskManager.service;

import org.seenu.taskManager.dto.TaskResponceDto;
import org.seenu.taskManager.dto.UserTaskSaveRequestDto;
import org.seenu.taskManager.entity.Task;
import org.seenu.taskManager.entity.TaskUser;
import org.seenu.taskManager.repository.TaskRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    TaskRepository taskRepository;
    TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    private TaskUser getTheContextUser()
    {
        TaskUser currentUser = (TaskUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return currentUser;
    }
    public String addNewTask(UserTaskSaveRequestDto userTaskSaveRequestDto) {
        TaskUser currentUser=getTheContextUser();
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

    public List<TaskResponceDto> getAllOldTasks() {
        TaskUser currentUser=getTheContextUser();
        LocalDateTime today=LocalDateTime.now();
        LocalDateTime past=today.minusDays(15);
        List<Task> myalltasks=taskRepository.getUserTask(currentUser.getId(),today,past);
        List<TaskResponceDto> taskResponceDtoList=new ArrayList<TaskResponceDto>();
        myalltasks.stream().forEach(task->{
            TaskResponceDto taskResponceDto=new TaskResponceDto();
            taskResponceDto.setTaskName(task.getTaskName());
            taskResponceDto.setDueDate(task.getDueDate());
            taskResponceDto.setIspriority(task.isIspriority());
            taskResponceDto.setCompleted(task.isCompleted());
            taskResponceDtoList.add(taskResponceDto);

        });
        return taskResponceDtoList;
    }

    public List<TaskResponceDto> getAllNewTasks() {
        TaskUser currentUser=getTheContextUser();
        LocalDateTime today=LocalDateTime.now();
        LocalDateTime future=today.plusDays(15);
        List<Task> myallNewtasks=taskRepository.getUserTask(currentUser.getId(),future,today);
        List<TaskResponceDto> taskResponceDtoList=new ArrayList<TaskResponceDto>();
        myallNewtasks.stream().forEach(task->{
            TaskResponceDto taskResponceDto=new TaskResponceDto();
            taskResponceDto.setTaskName(task.getTaskName());
            taskResponceDto.setDueDate(task.getDueDate());
            taskResponceDto.setIspriority(task.isIspriority());
            taskResponceDto.setCompleted(task.isCompleted());
            taskResponceDtoList.add(taskResponceDto);

        });
        return taskResponceDtoList;
    }

    public List<TaskResponceDto> getAllTaskByDay(LocalDateTime day) {
        TaskUser currentUser=getTheContextUser();
        List<Task> todayTasks=taskRepository.findByDueDate(day);
        List<TaskResponceDto> taskResponceDtoList=new ArrayList<TaskResponceDto>();
        todayTasks.stream().forEach(task->{
            TaskResponceDto taskResponceDto=new TaskResponceDto();
            taskResponceDto.setTaskName(task.getTaskName());
            taskResponceDto.setDueDate(task.getDueDate());
            taskResponceDto.setIspriority(task.isIspriority());
            taskResponceDto.setCompleted(task.isCompleted());
            taskResponceDtoList.add(taskResponceDto);

        });
        return taskResponceDtoList;
    }
}
