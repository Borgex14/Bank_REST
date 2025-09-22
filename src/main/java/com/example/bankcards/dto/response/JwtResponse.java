package com.example.bankcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа аутентификации.
 * Содержит JWT токены и информацию о пользователе.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    /**
     * Access token для авторизации запросов
     */
    private String accessToken;

    /**
     * Тип токена (обычно "Bearer")
     */
    private String tokenType;

    /**
     * Время жизни access token в секундах
     */
    private Long expiresIn;

    /**
     * Refresh token для обновления access token
     */
    private String refreshToken;

    /**
     * Информация о аутентифицированном пользователе
     */
    private UserInfo user;

    /**
     * DTO с основной информацией о пользователе
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        /**
         * Уникальный идентификатор пользователя
         */
        private Long id;

        /**
         * Имя пользователя (логин)
         */
        private String username;

        /**
         * Email пользователя
         */
        private String email;

        /**
         * Имя пользователя
         */
        private String firstName;

        /**
         * Фамилия пользователя
         */
        private String lastName;

        /**
         * Роль пользователя в системе
         */
        private String role;
    }
}