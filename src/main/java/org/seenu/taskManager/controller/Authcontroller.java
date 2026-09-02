package org.seenu.taskManager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.seenu.taskManager.dto.UserLoginDto;
import org.seenu.taskManager.dto.UserSignUpRequestDto;
import org.seenu.taskManager.service.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Slf4j
@Controller
@RequestMapping("/public/auth")
public class Authcontroller {
    private final UserAuthService userAuthService;
    Authcontroller(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }
    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> signupNewUser (@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) throws Exception {
        log.info(userSignUpRequestDto.getName());
        String result=userAuthService.cachTheUser(userSignUpRequestDto);
        log.info("result={}", result);
        return ResponseEntity.ok(Map.of("message", result));
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@Valid @RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
        String result= userAuthService.validLogIn(userLoginDto,request);
        return ResponseEntity.ok(Map.of("message", result));
    }
    @PostMapping("/signup")
    public String VerifyOtpAndSave(@RequestParam(required = false) String otp,@RequestParam(required = false) String email){
        String result= userAuthService.signUp(otp,email);
        return ResponseEntity.ok(Map.of("message", result));
    }

}
