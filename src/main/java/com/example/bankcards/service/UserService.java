package com.example.bankcards.service;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;

import java.util.List;

/**
 * Сервис для управления пользователями.
 */
public interface UserService {

    /**
     * Получает информацию о текущем пользователе.
     */
    UserResponse getCurrentUser(String username);

    /**
     * Находит пользователя по имени пользователя.
     */
    User findByUsername(String username);

    /**
     * Находит пользователя по идентификатору.
     */
    User findById(Long userId);

    /**
     * Проверяет существование пользователя.
     */
    boolean existsByUsername(String username);

    /**
     * Получает список всех пользователей.
     */
    List<UserResponse> getAllUsers();
}