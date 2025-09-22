package com.example.bankcards.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO для запроса на перевод денежных средств между картами.
 * Содержит информацию об отправителе, получателе и сумме перевода.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    /**
     * Идентификатор карты-отправителя
     */
    @NotNull(message = "Source card ID is required")
    private Long fromCardId;

    /**
     * Идентификатор карты-получателя
     */
    @NotNull(message = "Target card ID is required")
    private Long toCardId;

    /**
     * Сумма перевода (должна быть больше 0)
     */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    /**
     * Валюта перевода
     */
    @NotNull(message = "Currency is required")
    private String currency;

    /**
     * Описание перевода (не обязательно)
     */
    private String description;
}