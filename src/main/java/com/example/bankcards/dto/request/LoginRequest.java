package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса аутентификации пользователя.
 * Содержит учетные данные для входа в систему.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * Имя пользователя
     */
    @NotBlank(message = "Username is required")
    private String username;

    /**
     * Пароль пользователя
     */
    @NotBlank(message = "Password is required")
    private String password;
}