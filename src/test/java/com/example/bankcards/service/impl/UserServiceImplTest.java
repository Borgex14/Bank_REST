package com.example.bankcards.service.impl;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setRole(Role.USER);
        testUser.setIsActive(true);
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getCurrentUser_ValidUsername_ReturnsUserResponse() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserResponse response = userService.getCurrentUser("testuser");

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void getCurrentUser_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getCurrentUser("nonexistent");
        });
    }

    @Test
    void findByUsername_ValidUsername_ReturnsUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        User result = userService.findByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void findByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findByUsername("nonexistent");
        });
    }

    @Test
    void findById_ValidId_ReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_UserNotFound_ThrowsException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(999L);
        });
    }

    @Test
    void existsByUsername_UserExists_ReturnsTrue() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        boolean exists = userService.existsByUsername("testuser");

        assertTrue(exists);
    }

    @Test
    void existsByUsername_UserNotExists_ReturnsFalse() {
        when(userRepository.existsByUsername("nonexistent")).thenReturn(false);

        boolean exists = userService.existsByUsername("nonexistent");

        assertFalse(exists);
    }

    @Test
    void getAllUsers_ReturnsListOfUserResponses() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setRole(Role.USER);
        user2.setIsActive(true);
        user2.setCreatedAt(LocalDateTime.now());

        when(userRepository.findAll()).thenReturn(List.of(testUser, user2));

        List<UserResponse> responses = userService.getAllUsers();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("testuser", responses.get(0).getUsername());
        assertEquals("user2", responses.get(1).getUsername());
    }

    @Test
    void getAllUsers_EmptyList_ReturnsEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserResponse> responses = userService.getAllUsers();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void mapToResponse_ConvertsUserToResponseCorrectly() {
        UserResponse response = userService.mapToResponse(testUser);

        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test", response.getFirstName());
        assertEquals("User", response.getLastName());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    void convertToResponse_ConvertsUserToResponseWithRoleAndStatus() {
        UserResponse response = userService.convertToResponse(testUser);

        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test", response.getFirstName());
        assertEquals("User", response.getLastName());
        assertEquals("USER", response.getRole());
        assertTrue(response.getIsActive());
        assertNotNull(response.getCreatedAt());
    }
}