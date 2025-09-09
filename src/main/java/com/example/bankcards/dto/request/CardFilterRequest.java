package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Pattern(regexp = "^(true|false)$", message = "isBlocked must be 'true' or 'false'")
    private String isBlocked;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAfter;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdBefore;
}