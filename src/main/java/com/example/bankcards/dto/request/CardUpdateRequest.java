package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.CardStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardUpdateRequest {

    @Size(max = 100, message = "Card holder name must not exceed 100 characters")
    private String cardHolderName;

    private CardStatus status;

    private Boolean isActive;
}