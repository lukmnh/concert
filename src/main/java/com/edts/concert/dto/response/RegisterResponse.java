package com.edts.concert.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private String name;
    private String email;
    private String role;
}
