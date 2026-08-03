package org.seenu.taskManager.util;

import org.seenu.taskManager.dto.TaskResponceDto;
import org.seenu.taskManager.entity.Task;
import org.seenu.taskManager.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskUtil {
    public static List<TaskResponceDto> giveMyTask(List<Task> tasks) {
        List<TaskResponceDto> taskResponceDtoList = new ArrayList<>();
        tasks.stream().forEach(task->{
            TaskResponceDto taskResponceDto=new TaskResponceDto();
            taskResponceDto.setId(task.getId());
            taskResponceDto.setTaskName(task.getTaskName());
            taskResponceDto.setDueDate(task.getDueDate());
            taskResponceDto.setIspriority(task.isIspriority());
            taskResponceDto.setCompleted(task.isCompleted());
            taskResponceDtoList.add(taskResponceDto);

        });
        return taskResponceDtoList;
    }

}
