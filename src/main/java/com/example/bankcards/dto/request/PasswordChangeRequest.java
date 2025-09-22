package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса смены пароля пользователя.
 * Содержит текущий и новый пароли с подтверждением.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

    /**
     * Текущий пароль пользователя
     */
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    /**
     * Новый пароль (минимум 6 символов)
     */
    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "New password must be at least 6 characters")
    private String newPassword;

    /**
     * Подтверждение нового пароля
     */
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}