package org.seenu.taskManager.service;

import org.seenu.taskManager.entity.Task;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PriorityFilterService implements FilterService{
    @Override
    public List<Task> getMyTasksByFilter() {
        return null;
    }

    @Override
    public String getFilterType() {
        return "priority";
    }
}
