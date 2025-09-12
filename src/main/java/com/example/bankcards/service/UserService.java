package com.example.bankcards.service;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;

import java.util.List;

public interface UserService {
    UserResponse getCurrentUser(String username);
    User findByUsername(String username);
    User findById(Long userId);
    boolean existsByUsername(String username);
    List<UserResponse> getAllUsers();
}