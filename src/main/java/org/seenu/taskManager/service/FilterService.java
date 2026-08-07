package org.seenu.taskManager.service;

import org.seenu.taskManager.entity.Task;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface FilterService {
    public List<Task> getMyTasksByFilter();
    public String getFilterType();
}
