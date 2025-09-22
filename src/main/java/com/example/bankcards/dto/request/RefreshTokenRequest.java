package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса обновления JWT токена.
 * Содержит refresh token для получения новой пары токенов.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

    /**
     * Refresh token для обновления access token
     */
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}