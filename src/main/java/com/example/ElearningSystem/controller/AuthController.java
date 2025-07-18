package com.example.ElearningSystem.controller;

import com.example.ElearningSystem.model.LoginRequest;
import com.example.ElearningSystem.model.PreviousPasswords;
import com.example.ElearningSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/token")
public class AuthController {

    @Autowired
    private UserService userService;
    @PostMapping("login")
    public ResponseEntity<String> verify(@RequestBody LoginRequest loginRequest) throws NoSuchMethodException {
        return userService.verify(loginRequest);
    }

}
