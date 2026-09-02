package com.orderhub.controller;

import com.orderhub.domain.User;
import com.orderhub.dto.LoginRequest;
import com.orderhub.dto.LoginResponse;
import com.orderhub.dto.RegisterRequest;
import com.orderhub.dto.UserResponse;
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
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(
                request.getId(),
                request.getEmail(),
                request.getName(),
                request.getPassword()
        );

        return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getRole());
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        return new LoginResponse(token);
    }




}
