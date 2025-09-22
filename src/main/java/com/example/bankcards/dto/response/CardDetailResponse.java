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

/**
 * DTO для ответа с детальной информацией о банковской карте.
 * Содержит полные данные карты включая мета-информацию.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailResponse {

    /**
     * Уникальный идентификатор карты
     */
    private Long id;

    /**
     * Номер карты (маскированный или полный)
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
     * Валюта карты
     */
    private String currency;

    /**
     * Статус карты (ACTIVE, BLOCKED, EXPIRED)
     */
    private CardStatus status;

    /**
     * Тип карты (DEBIT, CREDIT)
     */
    private CardType type;

    /**
     * Дата и время создания карты
     */
    private LocalDateTime createdAt;

    /**
     * Дата и время последнего обновления карты
     */
    private LocalDateTime updatedAt;

    /**
     * Идентификатор пользователя-владельца карты
     */
    private Long userId;

    /**
     * Имя пользователя-владельца карты
     */
    private String username;

    /**
     * Флаг просроченности карты
     */
    private Boolean isExpired;
}