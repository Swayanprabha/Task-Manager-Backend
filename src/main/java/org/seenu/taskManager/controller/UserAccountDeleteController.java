package org.seenu.taskManager.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.seenu.taskManager.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserAccountDeleteController {
    private final MyUserService myUserService;
    @Autowired
    public UserAccountDeleteController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUserAccount(HttpServletRequest request)
    {
       myUserService.deleteMyAccount();
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        return ResponseEntity.ok("Account deleted successfully");
    }

}
