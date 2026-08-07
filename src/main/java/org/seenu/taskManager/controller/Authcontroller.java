package org.seenu.taskManager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.seenu.taskManager.dto.UserLoginDto;
import org.seenu.taskManager.dto.UserSignUpRequestDto;
import org.seenu.taskManager.service.UserSignUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
@Slf4j
@Controller
@RequestMapping("/public/auth")
public class Authcontroller {
    private final UserSignUpService userSignUpService;
    Authcontroller(UserSignUpService userSignUpService) {
        this.userSignUpService = userSignUpService;
    }
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signupNewUser (@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) throws Exception {
        log.info(userSignUpRequestDto.getName());
        String result=userSignUpService.signUp(userSignUpRequestDto);
        log.info("result={}", result);
        return ResponseEntity.ok(Map.of("message", result));
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@Valid @RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
        String result=userSignUpService.validLogIn(userLoginDto,request);
        return ResponseEntity.ok(Map.of("message", result));
    }
}
