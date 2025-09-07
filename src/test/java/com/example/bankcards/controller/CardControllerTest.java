package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.enums.CardType;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
class CardControllerTest extends AbstractControllerTest {

    @MockBean
    private CardService cardService;

    @MockBean
    private SecurityUtils securityUtils;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCard_AsAdmin_ShouldReturnCardResponse() throws Exception {
        CardCreateRequest request = new CardCreateRequest("JOHN DOE", CardType.DEBIT, "USD", BigDecimal.valueOf(1000));
        CardResponse response = CardResponse.builder()
                .id(1L)
                .cardNumber("****1234")
                .cardHolderName("JOHN DOE")
                .build();

        when(securityUtils.getCurrentUsername()).thenReturn("admin");
        when(cardService.createCard(any(), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/cards")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cardHolderName").value("JOHN DOE"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getUserCards_ShouldReturnPageOfCards() throws Exception {
        CardResponse card = CardResponse.builder()
                .id(1L)
                .cardNumber("****1234")
                .balance(BigDecimal.valueOf(1000))
                .build();
        Page<CardResponse> page = new PageImpl<>(List.of(card));

        when(securityUtils.getCurrentUsername()).thenReturn("user");
        when(cardService.getUserCards(any(), anyString(), any())).thenReturn(page);

        mockMvc.perform(get("/api/cards")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].balance").value(1000));
    }

    @Test
    @WithMockUser(roles = "USER")
    void blockCard_ShouldReturnCardResponse() throws Exception {
        CardResponse response = CardResponse.builder()
                .id(1L)
                .cardNumber("****1234")
                .build();

        when(securityUtils.getCurrentUsername()).thenReturn("user");
        when(cardService.blockCard(anyLong(), anyString())).thenReturn(response);

        mockMvc.perform(patch("/api/cards/1/block")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createCard_AsUser_ShouldReturnForbidden() throws Exception {
        CardCreateRequest request = new CardCreateRequest("JOHN DOE", CardType.DEBIT, "USD", BigDecimal.valueOf(1000));

        mockMvc.perform(post("/api/cards")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}