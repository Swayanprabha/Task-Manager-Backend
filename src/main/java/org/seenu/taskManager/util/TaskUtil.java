package org.seenu.taskManager.util;

import org.seenu.taskManager.dto.TaskResponseDto;
import org.seenu.taskManager.entity.Task;
import java.util.ArrayList;
import java.util.List;


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

}
