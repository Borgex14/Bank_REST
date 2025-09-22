package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.CardStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса на обновление данных карты.
 * Все поля опциональны - обновляются только переданные значения.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardUpdateRequest {

    /**
     * Новое имя владельца карты (не обязательно)
     */
    @Size(max = 100, message = "Card holder name must not exceed 100 characters")
    private String cardHolderName;

    /**
     * Новый статус карты
     */
    private CardStatus status;

    /**
     * Флаг активности карты
     */
    private Boolean isActive;
}