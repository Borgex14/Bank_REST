package com.example.bankcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией о балансе карты или счета.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {

    /**
     * Текущий баланс
     */
    private BigDecimal balance;

    /**
     * Валюта баланса (3-символьный код)
     */
    private String currency;

    /**
     * Время последнего обновления баланса
     */
    private LocalDateTime lastUpdated;
}