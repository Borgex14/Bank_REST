package com.example.bankcards.service;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;

public interface UserService {
    UserResponse getCurrentUser(String username);
    User findByUsername(String username);
    User findById(Long userId);
    boolean existsByUsername(String username);
}