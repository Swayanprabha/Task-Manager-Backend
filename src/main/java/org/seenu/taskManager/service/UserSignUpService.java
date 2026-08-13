package org.seenu.taskManager.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.seenu.taskManager.ExceptionHandle.UserAlreadyExistsException;
import org.seenu.taskManager.dto.UserLoginDto;
import org.seenu.taskManager.dto.UserSignUpRequestDto;
import org.seenu.taskManager.entity.TaskUser;
import org.seenu.taskManager.repository.TaskUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSignUpService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TaskUserRepository taskUserRepository;
    private final AuthenticationManager authenticationManager;
    UserSignUpService(TaskUserRepository taskUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager)
    {
        this.taskUserRepository = taskUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public String signUp( UserSignUpRequestDto userSignUpRequestDto) throws Exception {
        String mail=userSignUpRequestDto.getEmail();
        Optional<TaskUser> taskuser=taskUserRepository.findByEmail(mail);
        if(taskuser.isPresent())throw new UserAlreadyExistsException("this user already exists");
        String encriptedPassword=bCryptPasswordEncoder.encode(userSignUpRequestDto.getPassword());
        TaskUser newUser = new TaskUser();
        newUser.setName(userSignUpRequestDto.getName());
        newUser.setEmail(userSignUpRequestDto.getEmail());
        newUser.setPassword(encriptedPassword);
        taskUserRepository.save(newUser);
        return "User signed up successfully with email: "+mail;

    }

    public String validLogIn(@Valid UserLoginDto userLoginDto, HttpServletRequest request) {
   Authentication auth=authenticationManager.
           authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(),userLoginDto.getPassword()));
            var v1 = auth.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
        TaskUser currentUser = (TaskUser) auth.getPrincipal();
        return "User logged in successfully with email: "+currentUser.getUsername();
    }
}
