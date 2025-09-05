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
public class TransferResponse {

    private Long id;
    private String transactionId;
    private Long fromCardId;
    private String fromCardNumber;
    private Long toCardId;
    private String toCardNumber;
    private BigDecimal amount;
    private BigDecimal fee;
    private String currency;
    private TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;
    private String merchantName;
}