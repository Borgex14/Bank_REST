package com.example.bankcards.controller;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.entity.enums.TransactionStatus;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.util.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferController.class)
class TransferControllerTest extends AbstractControllerTest {

    @MockBean
    private TransferService transferService;

    @MockBean
    private SecurityUtils securityUtils;

    @Test
    @WithMockUser(roles = "USER")
    void transfer_ShouldReturnTransferResponse() throws Exception {
        TransferRequest request = new TransferRequest(1L, 2L, BigDecimal.valueOf(100), "USD", "Test transfer");
        TransferResponse response = TransferResponse.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(100))
                .status(TransactionStatus.COMPLETED)
                .build();

        when(securityUtils.getCurrentUsername()).thenReturn("user");
        when(transferService.transferBetweenOwnCards(any(), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/transfers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getTransferDetails_ShouldReturnTransferResponse() throws Exception {
        TransferResponse response = TransferResponse.builder()
                .id(1L)
                .transactionId("TXN123")
                .amount(BigDecimal.valueOf(100))
                .build();

        when(securityUtils.getCurrentUsername()).thenReturn("user");
        when(transferService.getTransferDetails(anyLong(), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/transfers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.transactionId").value("TXN123"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void transfer_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        TransferRequest invalidRequest = new TransferRequest(null, null, BigDecimal.valueOf(-100), "", "");

        mockMvc.perform(post("/api/transfers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}