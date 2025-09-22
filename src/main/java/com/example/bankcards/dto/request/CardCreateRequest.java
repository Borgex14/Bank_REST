package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.CardType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO для запроса на создание новой банковской карты.
 * Содержит все необходимые данные для создания карты.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCreateRequest {

    /**
     * Имя владельца карты
     */
    @NotBlank(message = "Card holder name is required")
    @Size(max = 100, message = "Card holder name must not exceed 100 characters")
    private String cardHolderName;

    /**
     * Тип банковской карты (DEBIT, CREDIT и т.д.)
     */
    @NotNull(message = "Card type is required")
    private CardType type;

    /**
     * Валюта карты (3-символьный код, например: USD, EUR)
     */
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    private String currency;

    /**
     * Начальный баланс карты (должен быть положительным)
     */
    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", message = "Initial balance must be positive")
    private BigDecimal initialBalance;
}