package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailResponse {

    private Long id;
    private String cardNumber;
    private String cardHolderName;
    private LocalDate expirationDate;
    private BigDecimal balance;
    private String currency;
    private CardStatus status;
    private CardType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String username;
    private Boolean isExpired;
}