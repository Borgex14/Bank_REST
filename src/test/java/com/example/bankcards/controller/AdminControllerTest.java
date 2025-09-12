package com.example.bankcards.controller;

import com.example.bankcards.config.TestSecurityConfig;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.JwtService;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(TestSecurityConfig.class)
class AdminControllerIntegrationTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getAllUsers_WithRealAdminUser_ShouldReturnUsersList() throws Exception {
        User adminUser = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@email.com")
                .password("encoded")
                .firstName("John")
                .lastName("Doe")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(jwtService.isTokenValid(any(), any())).thenReturn(true);

        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .username("admin")
                .email("admin@email.com")
                .firstName("John")
                .lastName("Doe")
                .role("ADMIN")
                .build();

        when(userService.getAllUsers()).thenReturn(List.of(userResponse));

        Authentication auth = new UsernamePasswordAuthenticationToken(
                adminUser, null, adminUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[0].role").value("ADMIN"));
    }

    @Test
    void getAllUsers_WithRealUserRole_ShouldReturnForbidden() throws Exception {
        User regularUser = User.builder()
                .id(2L)
                .username("user")
                .email("user@email.com")
                .password("encoded")
                .firstName("Jane")
                .lastName("Smith")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(regularUser));

        Authentication auth = new UsernamePasswordAuthenticationToken(
                regularUser, null, regularUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isForbidden());
    }
}