package com.edts.concert.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    public String generateAccessToken(UserDetails userDetails);
    public String generateRefreshToken(UserDetails userDetails);
    public String extractUsername(String token);
    public boolean isTokenValid(String token, UserDetails userDetails);
    String generateRefreshTokenValue();
}
