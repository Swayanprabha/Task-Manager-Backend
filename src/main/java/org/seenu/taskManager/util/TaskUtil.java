package org.seenu.taskManager.util;

import org.seenu.taskManager.ExceptionHandle.InvalidUserException;
import org.seenu.taskManager.dto.TaskResponseDto;
import org.seenu.taskManager.entity.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskUtil {
    public static List<TaskResponseDto> giveMyTask(List<Task> tasks) {
        List<TaskResponseDto> taskResponseDtoList = new ArrayList<>();
        tasks.stream().forEach(task->{
            TaskResponseDto taskResponseDto =new TaskResponseDto();
            taskResponseDto.setId(task.getId());
            taskResponseDto.setTaskName(task.getTaskName());
            taskResponseDto.setDueDate(task.getDueDate());
            taskResponseDto.setIspriority(task.isIspriority());
            taskResponseDto.setCompleted(task.isCompleted());
            taskResponseDtoList.add(taskResponseDto);

        });
        return taskResponseDtoList;
    }
    public LocalDate validateDate(String date) {
        try
        {
            return LocalDate.parse(date);
        }catch (Exception e)
        {
            throw new InvalidUserException("invalid date format, please enter as YYYY-MM-DD");
        }
    }

}
