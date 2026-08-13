package org.seenu.taskManager.service;

import org.seenu.taskManager.dto.TaskResponseDto;
import org.seenu.taskManager.entity.Task;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface FilterService {
    public  List<TaskResponseDto> getMyTasksByFilter(List<TaskResponseDto> allTasksOfDay);
    public String getFilterType();
}
