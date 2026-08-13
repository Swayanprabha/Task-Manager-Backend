package org.seenu.taskManager.service;

import org.seenu.taskManager.dto.TaskResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CompletedFilterService implements FilterService{
    @Override
    public  List<TaskResponseDto> getMyTasksByFilter(List<TaskResponseDto> allTasksOfDay) {

        List<TaskResponseDto> completedTasks=allTasksOfDay.stream()
                .filter(TaskResponseDto::isCompleted)
                .toList();
        return completedTasks;
    }
    @Override
    public String getFilterType()
    {
        return "completed";
    }

}
