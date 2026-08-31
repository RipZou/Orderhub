package com.orderhub.controller;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    public @NotBlank String getEmail() {
        return email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public void setEmail(@NotBlank String email) {
        this.email = email;
    }

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
