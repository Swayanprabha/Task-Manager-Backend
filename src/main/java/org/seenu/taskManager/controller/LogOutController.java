package org.seenu.taskManager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mylogout")
public class LogOutController {
    @PostMapping()
    public ResponseEntity<String> logout(HttpServletRequest request)
    {
        HttpSession session=request.getSession(false);
        if(session!=null)
        {
            session.invalidate();
            return ResponseEntity.ok("Logged out successfully");
        }
        else {
            return ResponseEntity.ok("No active session found");
        }
    }

}
