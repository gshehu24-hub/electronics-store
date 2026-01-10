package com.electronicstore.controller;

import com.electronicstore.service.AuthenticationService;

public class AuthenticationController {
    private AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    public boolean authenticate(String user, String pass) {
        return authService.authenticate(user, pass);
    }
}
