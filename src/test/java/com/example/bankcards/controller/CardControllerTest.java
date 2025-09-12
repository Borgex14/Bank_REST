package com.example.bankcards.controller;

import com.example.bankcards.config.TestSecurityConfig;
import com.example.bankcards.config.WebMvcConfig;
import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.enums.CardType;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
@Import({TestSecurityConfig.class, WebMvcConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class CardControllerTest extends AbstractControllerTest {

    @MockBean
    private CardService cardService;

    @MockBean
    private SecurityUtils securityUtils;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCard_AsAdmin_ShouldReturnCardResponse() throws Exception {
        CardCreateRequest request = new CardCreateRequest("JOHN DOE", CardType.DEBIT, "USD", BigDecimal.valueOf(1000));
        CardResponse response = CardResponse.builder()
                .id(1L)
                .cardNumber("****1234")
                .cardHolderName("JOHN DOE")
                .balance(BigDecimal.valueOf(1000))
                .build();

        when(securityUtils.getCurrentUsername()).thenReturn("admin");
        when(cardService.createCard(any(CardCreateRequest.class), eq("admin"))).thenReturn(response);

        mockMvc.perform(post("/api/cards")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cardHolderName").value("JOHN DOE"))
                .andExpect(jsonPath("$.balance").value(1000));

        verify(cardService).createCard(any(CardCreateRequest.class), eq("admin"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void blockCard_AsUser_ShouldReturnCardResponse() throws Exception {
        // Arrange
        CardResponse response = CardResponse.builder()
                .id(1L)
                .cardNumber("****1234")
                .isBlocked(true)
                .build();

        when(securityUtils.getCurrentUsername()).thenReturn("user");
        when(cardService.blockCard(eq(1L), eq("user"))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/api/cards/1/block")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isBlocked").value(true));

        verify(cardService).blockCard(eq(1L), eq("user"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void blockCard_AsAdmin_ShouldReturnCardResponse() throws Exception {
        // Arrange
        CardResponse response = CardResponse.builder()
                .id(2L)
                .cardNumber("****5678")
                .isBlocked(true)
                .build();

        when(securityUtils.getCurrentUsername()).thenReturn("admin");
        when(cardService.blockCard(eq(2L), eq("admin"))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/api/cards/2/block")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.isBlocked").value(true));

        verify(cardService).blockCard(eq(2L), eq("admin"));
    }
}