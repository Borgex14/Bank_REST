package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardFilterRequest {

    private CardStatus status;
    private CardType type;
    private String currency;
    private BigDecimal minBalance;
    private BigDecimal maxBalance;
    private LocalDate expirationDateFrom;
    private LocalDate expirationDateTo;
    private Boolean isExpired;
    private Long userId;
}