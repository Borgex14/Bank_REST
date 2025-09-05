package com.example.bankcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {

    private Long totalUsers;
    private Long totalCards;
    private Long totalTransactions;
    private BigDecimal totalBalance;
    private Map<String, Long> cardsByType;
    private Map<String, Long> cardsByStatus;
    private Map<String, BigDecimal> transactionsByStatus;
    private Map<String, Long> transactionsByDay;
}