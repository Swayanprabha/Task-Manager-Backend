package org.seenu.taskManager.service;

import org.seenu.taskManager.dto.TaskResponseDto;
import org.seenu.taskManager.entity.Task;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PriorityFilterService implements FilterService{
    @Override
    public  List<TaskResponseDto> getMyTasksByFilter(List<TaskResponseDto> allTasksOfDay) {
        return allTasksOfDay.stream().filter(TaskResponseDto::isIspriority).
                toList();
    }

    @Override
    public String getFilterType() {
        return "priority";
    }
}
