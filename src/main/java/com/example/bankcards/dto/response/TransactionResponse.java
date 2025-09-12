package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long id;
    private String transactionId;
    private CardInfo fromCard;
    private CardInfo toCard;
    private BigDecimal amount;
    private BigDecimal fee;
    private String currency;
    private TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String merchantName;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardInfo {
        private Long id;
        private String cardNumber;
        private String cardHolderName;
    }
}