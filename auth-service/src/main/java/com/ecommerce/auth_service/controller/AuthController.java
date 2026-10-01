package com.ecommerce.auth_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.auth_service.dto.LoginRequest;
import com.ecommerce.auth_service.dto.LoginResponse;
import com.ecommerce.auth_service.service.KeycloakTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakTokenService keycloakTokenService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return keycloakTokenService.login(request);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestBody LoginResponse request) {
        return keycloakTokenService.refresh(request.getRefreshToken());
    }
}