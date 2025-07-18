package com.example.ElearningSystem.controller;

import com.example.ElearningSystem.model.PreviousPasswords;
import com.example.ElearningSystem.model.Users;
import com.example.ElearningSystem.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("adminControl/getAllUsers")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("student/addAUser")
    public ResponseEntity<Object> addAUser(@RequestBody Users users) throws MessagingException {
        return userService.addAUser(users);
    }

    @GetMapping("adminControl/getByUsername")
    public UserDetails getByUsername(@PathParam("username") String username) {
        return userService.loadUserByUsername(username);
    }

    @PutMapping("adminControl/updateUser/{username}")
    public String updateTheUser(@PathVariable("username") String username, @RequestBody Users users) {
        return userService.updateTheUser(username, users);
    }

    @DeleteMapping("adminControl/deleteUser/{username}")
    public String deleteTheUser(@PathVariable("username") String username) {
        return userService.deleteTheUser(username);
    }
    @PatchMapping("adminControl/updateUserUnLockStatus/{username}")
    public ResponseEntity<String> updateUserAccountStatus(
            @PathVariable("username") String username){
       return userService.updateLockStatus(username);
    }
    @PatchMapping("adminControl/archiveTheUser/{username}")
public ResponseEntity<String> archiveTheUser(@PathVariable("username") String username){
        return userService.archiveTheUser(username);
    }
    @PostMapping("student/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("resetToken") String token,
                                           @RequestParam("newPassword") String newPassword) {
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Account is activated");
    }
    @GetMapping("student/forgot-password/{username}")
    public ResponseEntity<String> forgotPasswordRequest(@PathVariable("username") String username)
            throws MessagingException {
        return userService.forgotPassword(username);
    }
    @PostMapping("student/addPasswords")
    public List<PreviousPasswords> addPasswords(@RequestBody List<PreviousPasswords> passwords){
        return userService.addPasswords(passwords);
    }
}
