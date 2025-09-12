package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.CardStatus;
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
public class CardResponse {
    private Long id;
    private String cardNumber;
    private String cardHolderName;
    private LocalDate expirationDate;
    private BigDecimal balance;
    private Boolean isBlocked;
    private String currency;
    private CardStatus status;
    private LocalDateTime createdAt;
    private String type;
}