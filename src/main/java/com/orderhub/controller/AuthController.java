package com.orderhub.controller;


import com.orderhub.domain.User;
import com.orderhub.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody RigisterRequest request) {
        return authService.register(
                request.getId(),
                request.getEmail(),
                request.getName(),
                request.getPassword()
        );
    }



}
