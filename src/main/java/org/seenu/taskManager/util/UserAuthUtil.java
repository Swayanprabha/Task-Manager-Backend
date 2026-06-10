package org.seenu.taskManager.util;

import org.seenu.taskManager.entity.TaskUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthUtil {
    public Long returnTaskUserId() {
        TaskUser currentUser = (TaskUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return currentUser.getId();
    }
    public TaskUser getTheContextUser()
    {
        TaskUser currentUser = (TaskUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return currentUser;
    }

}
