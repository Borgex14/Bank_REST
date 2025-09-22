package com.example.bankcards.service;

import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.request.RegisterRequest;
import com.example.bankcards.dto.response.JwtResponse;

/**
 * Сервис для аутентификации и авторизации пользователей.
 */
public interface AuthService {

    /**
     * Регистрирует нового пользователя в системе.
     */
    JwtResponse register(RegisterRequest request);

    /**
     * Выполняет аутентификацию пользователя.
     */
    JwtResponse login(LoginRequest request);

    /**
     * Выполняет выход пользователя из системы.
     */
    void logout(String token);
}