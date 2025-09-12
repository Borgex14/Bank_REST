package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.RegisterRequest;
import com.example.bankcards.dto.response.JwtResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.exception.AuthenticationException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_FirstUser_ShouldBeAdmin() {
        RegisterRequest request = new RegisterRequest(
                "admin", "password", "admin@email.com", "John", "Doe"
        );

        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(userRepository.existsByEmail("admin@email.com")).thenReturn(false);
        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(jwtService.generateToken(any())).thenReturn("jwtToken");
        when(jwtService.getJwtExpiration()).thenReturn(3600000L);

        User savedUser = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@email.com")
                .password("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();

        when(userRepository.save(any())).thenReturn(savedUser);

        JwtResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("ADMIN", response.getUser().getRole());
        verify(userRepository).count();
    }

    @Test
    void register_SubsequentUser_ShouldBeUser() {
        RegisterRequest request = new RegisterRequest(
                "user", "password", "user@email.com", "Jane", "Smith"
        );

        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("user@email.com")).thenReturn(false);
        when(userRepository.count()).thenReturn(1L); // Уже есть пользователи
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(jwtService.generateToken(any())).thenReturn("jwtToken");
        when(jwtService.getJwtExpiration()).thenReturn(3600000L);

        User savedUser = User.builder()
                .id(2L)
                .username("user")
                .email("user@email.com")
                .password("encodedPassword")
                .firstName("Jane")
                .lastName("Smith")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();

        when(userRepository.save(any())).thenReturn(savedUser);

        JwtResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("USER", response.getUser().getRole());
        verify(userRepository).count();
    }

    @Test
    void register_UsernameAlreadyExists_ShouldThrowException() {
        // Arrange
        RegisterRequest request = new RegisterRequest(
                "existing", "password", "test@email.com", "Test", "User"
        );

        when(userRepository.existsByUsername("existing")).thenReturn(true);

        assertThrows(AuthenticationException.class, () -> authService.register(request));
        verify(userRepository, never()).count();
    }
}