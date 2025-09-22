package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией о переводе средств.
 * Специализированная версия для операций перевода между картами.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

    /**
     * Уникальный идентификатор перевода
     */
    private Long id;

    /**
     * Публичный идентификатор транзакции перевода
     */
    private String transactionId;

    /**
     * Идентификатор карты-отправителя
     */
    private Long fromCardId;

    /**
     * Номер карты-отправителя
     */
    private String fromCardNumber;

    /**
     * Идентификатор карты-получателя
     */
    private Long toCardId;

    /**
     * Номер карты-получателя
     */
    private String toCardNumber;

    /**
     * Сумма перевода
     */
    private BigDecimal amount;

    /**
     * Комиссия за перевод
     */
    private BigDecimal fee;

    /**
     * Валюта перевода
     */
    private String currency;

    /**
     * Статус перевода
     */
    private TransactionStatus status;

    /**
     * Описание перевода
     */
    private String description;

    /**
     * Дата и время создания перевода
     */
    private LocalDateTime createdAt;

    /**
     * Название мерчанта (если применимо)
     */
    private String merchantName;

    /**
     * Временная метка операции (дублирует createdAt для совместимости)
     */
    private LocalDateTime timestamp;
}