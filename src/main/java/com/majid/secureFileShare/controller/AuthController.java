package com.majid.secureFileShare.controller;

import com.majid.secureFileShare.dataTransferObject.LoginRequest;
import com.majid.secureFileShare.dataTransferObject.RegisterRequest;
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
    public ResponseEntity<String> register (@RequestBody RegisterRequest registerRequest) {
        userService.registerUser(registerRequest.email(), registerRequest.password());
                return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity <String> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticatedLogin = userService.authenticate(loginRequest.email(), loginRequest.password());
        if(isAuthenticatedLogin) {
            return ResponseEntity.ok("Login successful");
        }
        else {
            return ResponseEntity.ok("invalid email or password");
        }

    }




}
