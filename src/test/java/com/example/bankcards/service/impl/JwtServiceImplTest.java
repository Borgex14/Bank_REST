package com.example.bankcards.service.impl;

import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private UserDetails userDetails;

    @Mock
    private User user;

    private final String testUsername = "testuser";
    private final String testToken = "test.token.here";
    private final long expiration = 3600000;

    @BeforeEach
    void setUp() {
        jwtService.secretKey = "testSecretKey123456789012345678901234567890";
        jwtService.jwtExpiration = expiration;
    }

    @Test
    void extractUsername_ValidToken_ReturnsUsername() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testUsername);
        String token = jwtService.generateToken(userDetails);

        // Act
        String result = jwtService.extractUsername(token);

        // Assert
        assertEquals(testUsername, result);
    }

    @Test
    void generateToken_WithUserDetails_ReturnsValidToken() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testUsername);

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    void generateToken_WithUser_ReturnsTokenWithExtraClaims() {
        // Arrange
        when(user.getUsername()).thenReturn(testUsername);
        when(user.getId()).thenReturn(1L);
        when(user.getRole()).thenReturn(Role.USER);
        when(user.getEmail()).thenReturn("test@example.com");

        // Act
        String token = jwtService.generateToken(user);

        // Assert
        assertNotNull(token);
        String username = jwtService.extractUsername(token);
        assertEquals(testUsername, username);
    }

    @Test
    void isTokenValid_ValidToken_ReturnsTrue() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testUsername);
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_InvalidUsername_ReturnsFalse() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testUsername);
        String token = jwtService.generateToken(userDetails);

        UserDetails otherUser = mock(UserDetails.class);
        when(otherUser.getUsername()).thenReturn("otheruser");

        // Act
        boolean isValid = jwtService.isTokenValid(token, otherUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void isTokenValid_InvalidatedToken_ReturnsFalse() {
        // Arrange
        when(userDetails.getUsername()).thenReturn(testUsername);
        String token = jwtService.generateToken(userDetails);

        jwtService.invalidateToken(token);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void invalidateToken_TokenBecomesInvalid() {
        when(userDetails.getUsername()).thenReturn(testUsername);
        String token = jwtService.generateToken(userDetails);

        jwtService.invalidateToken(token);

        assertTrue(jwtService.isTokenInvalidated(token));
    }
}