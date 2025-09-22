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

/**
 * DTO для фильтрации карт при поиске.
 * Поддерживает фильтрацию по различным параметрам карт.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardFilterRequest {

    /**
     * Статус карты (ACTIVE, BLOCKED, EXPIRED)
     */
    private CardStatus status;

    /**
     * Тип карты (DEBIT, CREDIT)
     */
    private CardType type;

    /**
     * Валюта карты
     */
    private String currency;

    /**
     * Минимальный баланс карты
     */
    private BigDecimal minBalance;

    /**
     * Максимальный баланс карты
     */
    private BigDecimal maxBalance;

    /**
     * Дата окончания действия карты (с)
     */
    private LocalDate expirationDateFrom;

    /**
     * Дата окончания действия карты (по)
     */
    private LocalDate expirationDateTo;

    /**
     * Флаг просроченности карты
     */
    private Boolean isExpired;

    /**
     * Идентификатор пользователя-владельца карты
     */
    private Long userId;

    /**
     * Флаг блокировки карты (строковое значение "true" или "false")
     */
    @Pattern(regexp = "^(true|false)$", message = "isBlocked must be 'true' or 'false'")
    private String isBlocked;

    /**
     * Дата создания карты (после указанной даты)
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAfter;

    /**
     * Дата создания карты (до указанной даты)
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdBefore;
}