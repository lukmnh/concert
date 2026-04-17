package com.edts.concert.service;

import com.edts.concert.dto.request.AuthRequest;
import com.edts.concert.dto.request.RegisterRequest;
import com.edts.concert.dto.request.TokenRequest;
import com.edts.concert.dto.response.AuthResponse;
import com.edts.concert.dto.response.RegisterResponse;

public interface AuthService {
    public RegisterResponse register(RegisterRequest registerRequest);
    public AuthResponse login(AuthRequest authRequest);
    public AuthResponse refreshToken(TokenRequest tokenRequest);
    public void logout(TokenRequest tokenRequest);
}
