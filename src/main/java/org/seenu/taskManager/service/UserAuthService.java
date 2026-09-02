package org.seenu.taskManager.service;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.seenu.taskManager.ExceptionHandle.UserAlreadyExistsException;
import org.seenu.taskManager.dto.UserLoginDto;
import org.seenu.taskManager.dto.UserSignUpRequestDto;
import org.seenu.taskManager.entity.TaskUser;
import org.seenu.taskManager.repository.TaskUserRepository;
import org.seenu.taskManager.util.UserAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthService {
    @Autowired
    private Cache<String, UserSignUpRequestDto> pendingUserCache;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TaskUserRepository taskUserRepository;
    private final AuthenticationManager authenticationManager;
    private final UserAuthUtil userAuthUtil;
    UserAuthService(TaskUserRepository taskUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                    AuthenticationManager authenticationManager, UserAuthUtil userAuthUtil)
    {
        this.taskUserRepository = taskUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.userAuthUtil = userAuthUtil;
    }

    public String cachTheUser(UserSignUpRequestDto userSignUpRequestDto) {
        String mail=userSignUpRequestDto.getEmail();
        Optional<TaskUser> taskuser=taskUserRepository.findByEmail(mail);
        if(taskuser.isPresent())throw new UserAlreadyExistsException("this user already  exists");
        String currentUserOtp=userAuthUtil.getUserOtp();
        userSignUpRequestDto.setOtp(currentUserOtp);
        try
        {
            pendingUserCache.put(mail,userSignUpRequestDto);
            //mail to user here





            return "User added to cache successfully with email: "+mail;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error occurred while adding user to cache", e);
        }

    }
//    public String signUp( String otp,String mail){
//        UserSignUpRequestDto userSignUpRequestDto = pendingUserCache.getIfPresent(mail);
//        if (userSignUpRequestDto == null) {
//            throw new IllegalArgumentException("User not found in cache");
//        }
//        if (!userSignUpRequestDto.getOtp().equals(otp)) {
//            throw new IllegalArgumentException("Invalid OTP")
//        }
//        String encriptedPassword=bCryptPasswordEncoder.encode(userSignUpRequestDto.getPassword());
//        TaskUser newUser = new TaskUser();
//        newUser.setName(userSignUpRequestDto.getName());
//        newUser.setEmail(userSignUpRequestDto.getEmail());
//        newUser.setPassword(encriptedPassword);
//        taskUserRepository.save(newUser);
//        return "User signed up successfully with email: "+userSignUpRequestDto.getEmail();
//
//    }

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
