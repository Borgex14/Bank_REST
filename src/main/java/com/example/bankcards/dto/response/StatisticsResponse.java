package com.example.bankcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO для ответа со статистическими данными системы.
 * Содержит агрегированную информацию о пользователях, картах и транзакциях.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {

    /**
     * Общее количество пользователей в системе
     */
    private Long totalUsers;

    /**
     * Общее количество карт в системе
     */
    private Long totalCards;

    /**
     * Общее количество транзакций
     */
    private Long totalTransactions;

    /**
     * Суммарный баланс всех карт
     */
    private BigDecimal totalBalance;

    /**
     * Распределение карт по типам (тип -> количество)
     */
    private Map<String, Long> cardsByType;

    /**
     * Распределение карт по статусам (статус -> количество)
     */
    private Map<String, Long> cardsByStatus;

    /**
     * Распределение транзакций по статусам (статус -> сумма)
     */
    private Map<String, BigDecimal> transactionsByStatus;

    /**
     * Количество транзакций по дням (дата -> количество)
     */
    private Map<String, Long> transactionsByDay;
}