package com.majid.secureFileShare.controller;

import com.majid.secureFileShare.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;

    }

    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestParam String email,
                                            @RequestParam String password) {
        userService.registerUser(email, password);
                return ResponseEntity.ok("User registered successfully");
    }




}
