package org.seenu.taskManager.service;

import jakarta.transaction.Transactional;
import org.seenu.taskManager.entity.TaskUser;
import org.seenu.taskManager.repository.TaskUserRepository;
import org.seenu.taskManager.util.UserAuthUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {
    private final TaskUserRepository taskUserRepository;
    private final UserAuthUtil userAuthUtil;
    MyUserService(TaskUserRepository taskUserRepository, UserAuthUtil userAuthUtil)
    {
        this.taskUserRepository = taskUserRepository;
        this.userAuthUtil = userAuthUtil;
    }
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        UserDetails user=taskUserRepository.findByEmail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found: " ));
        return user;
    }
    @Transactional
    public void deleteMyAccount() {
        TaskUser currentUser=userAuthUtil.getTheContextUser();
        taskUserRepository.deleteMyTasks(currentUser.getId());
        taskUserRepository.delete(currentUser);

    }
}
