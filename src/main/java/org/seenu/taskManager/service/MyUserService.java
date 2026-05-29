package org.seenu.taskManager.service;

import org.seenu.taskManager.repository.TaskUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {
    private final TaskUserRepository taskUserRepository;
    MyUserService(TaskUserRepository taskUserRepository)
    {
        this.taskUserRepository = taskUserRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        UserDetails user=taskUserRepository.findByEmail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found: " ));
        return user;
    }
}
