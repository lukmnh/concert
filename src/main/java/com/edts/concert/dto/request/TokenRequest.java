package com.edts.concert.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest {
    @NotBlank(message = "Refresh token tidak boleh kosong")
    private String refreshToken;
}
