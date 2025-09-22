package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией о транзакции.
 * Содержит полные детали финансовой операции.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    /**
     * Уникальный идентификатор транзакции в системе
     */
    private Long id;

    /**
     * Публичный идентификатор транзакции (для клиента)
     */
    private String transactionId;

    /**
     * Информация о карте-отправителе
     */
    private CardInfo fromCard;

    /**
     * Информация о карте-получателе
     */
    private CardInfo toCard;

    /**
     * Сумма транзакции
     */
    private BigDecimal amount;

    /**
     * Комиссия за транзакцию
     */
    private BigDecimal fee;

    /**
     * Валюта транзакции
     */
    private String currency;

    /**
     * Статус транзакции (PENDING, COMPLETED, FAILED и т.д.)
     */
    private TransactionStatus status;

    /**
     * Описание транзакции
     */
    private String description;

    /**
     * Дата и время создания транзакции
     */
    private LocalDateTime createdAt;

    /**
     * Дата и время последнего обновления транзакции
     */
    private LocalDateTime updatedAt;

    /**
     * Название мерчанта (если применимо)
     */
    private String merchantName;

    /**
     * DTO с краткой информацией о карте
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardInfo {
        /**
         * Идентификатор карты
         */
        private Long id;

        /**
         * Номер карты
         */
        private String cardNumber;

        /**
         * Имя владельца карты
         */
        private String cardHolderName;
    }
}