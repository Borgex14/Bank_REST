package com.example.bankcards.controller;

import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.request.RegisterRequest;
import com.example.bankcards.dto.response.JwtResponse;
import com.example.bankcards.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends AbstractControllerTest {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void register_ShouldReturnJwtResponse() throws Exception {
        RegisterRequest request = new RegisterRequest("testuser", "password123",
                "test@email.com", "John", "Doe");

        JwtResponse response = JwtResponse.builder()
                .accessToken("token")
                .tokenType("Bearer")
                .expiresIn(3600L)
                .user(JwtResponse.UserInfo.builder()
                        .id(1L)
                        .username("testuser")
                        .email("test@email.com")
                        .firstName("John")
                        .lastName("Doe")
                        .role("USER")
                        .build())
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void login_ShouldReturnJwtResponse() throws Exception {
        LoginRequest request = new LoginRequest("testuser", "password123");

        JwtResponse response = JwtResponse.builder()
                .accessToken("token")
                .tokenType("Bearer")
                .expiresIn(3600L)
                .user(JwtResponse.UserInfo.builder()
                        .id(1L)
                        .username("testuser")
                        .email("test@email.com")
                        .firstName("John")
                        .lastName("Doe")
                        .role("USER")
                        .build())
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token"));
    }

    @Test
    void register_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        String invalidJson = """
        {
            "username": "",
            "password": "12",
            "email": "invalid-email",
            "firstName": "",
            "lastName": ""
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}