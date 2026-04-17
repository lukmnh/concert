package com.edts.concert.controller;

import com.edts.concert.dto.request.AuthRequest;
import com.edts.concert.dto.request.RegisterRequest;
import com.edts.concert.dto.request.TokenRequest;
import com.edts.concert.dto.response.AuthResponse;
import com.edts.concert.dto.response.RegisterResponse;
import com.edts.concert.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AuthResponse> refresh(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}
