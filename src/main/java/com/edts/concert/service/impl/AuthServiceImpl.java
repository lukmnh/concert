package com.edts.concert.service.impl;

import com.edts.concert.constant.Role;
import com.edts.concert.dto.request.AuthRequest;
import com.edts.concert.dto.request.RegisterRequest;
import com.edts.concert.dto.request.TokenRequest;
import com.edts.concert.dto.response.AuthResponse;
import com.edts.concert.dto.response.RegisterResponse;
import com.edts.concert.entity.RefreshToken;
import com.edts.concert.entity.User;
import com.edts.concert.exception.EmailAlreadyExistsException;
import com.edts.concert.exception.InvalidTokenException;
import com.edts.concert.repository.RefreshTokenRepository;
import com.edts.concert.repository.UserRepository;
import com.edts.concert.service.AuthService;
import com.edts.concert.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        log.info("inside register()");
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email sudah terdaftar");
        }

        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        RefreshToken refreshToken = createAndSaveRefreshToken(user);

        return RegisterResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(refreshToken.getToken())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        log.info("inside login()");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        refreshTokenRepository.deleteAllByUser(user);
        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse refreshToken(TokenRequest tokenRequest) {
        log.info("inside refreshToken()");
        RefreshToken stored = refreshTokenRepository.findByToken(tokenRequest.getRefreshToken())
                .orElseThrow(() -> new InvalidTokenException("Refresh token tidak ditemukan"));

        if (!stored.isValid()) {
            refreshTokenRepository.deleteAllByUser(stored.getUser());
            throw new InvalidTokenException("Refresh token expired atau sudah di-revoke");
        }

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);
        return buildAuthResponse(stored.getUser());
    }

    @Override
    public void logout(TokenRequest tokenRequest) {
        log.info("inside logout()");
        RefreshToken stored = refreshTokenRepository.findByToken(tokenRequest.getRefreshToken())
                .orElseThrow(() -> new InvalidTokenException("Refresh token tidak ditemukan"));
        refreshTokenRepository.deleteAllByUser(stored.getUser());
    }

    private AuthResponse buildAuthResponse(User user) {
        RefreshToken refreshToken = createAndSaveRefreshToken(user);
        return AuthResponse.builder()
                .token(jwtService.generateAccessToken(user))
                .refreshToken(refreshToken.getToken())
                .build();
    }

    private RefreshToken createAndSaveRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(jwtService.generateRefreshTokenValue())
                .expiredAt(LocalDateTime.now().plus(refreshExpirationMs, ChronoUnit.MILLIS))
                .revoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }
}
