package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO для ответа с основной информацией о банковской карте.
 * Используется для списков и краткого представления карт.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {

    /**
     * Уникальный идентификатор карты
     */
    private Long id;

    /**
     * Номер карты (обычно маскированный)
     */
    private String cardNumber;

    /**
     * Имя владельца карты
     */
    private String cardHolderName;

    /**
     * Дата окончания действия карты
     */
    private LocalDate expirationDate;

    /**
     * Текущий баланс карты
     */
    private BigDecimal balance;

    /**
     * Флаг блокировки карты
     */
    private Boolean isBlocked;

    /**
     * Валюта карты
     */
    private String currency;

    /**
     * Статус карты
     */
    private CardStatus status;

    /**
     * Дата и время создания карты
     */
    private LocalDateTime createdAt;

    /**
     * Тип карты в строковом представлении
     */
    private String type;
}