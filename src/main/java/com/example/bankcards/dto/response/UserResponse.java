package com.example.bankcards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для ответа с информацией о пользователе.
 * Содержит профиль пользователя и связанные с ним данные.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    /**
     * Уникальный идентификатор пользователя
     */
    private Long id;

    /**
     * Имя пользователя (логин)
     */
    private String username;

    /**
     * Email пользователя
     */
    private String email;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;

    /**
     * Роль пользователя в системе
     */
    private String role;

    /**
     * Флаг активности учетной записи
     */
    private Boolean isActive;

    /**
     * Дата и время регистрации пользователя
     */
    private LocalDateTime createdAt;

    /**
     * Дата и время последнего обновления профиля
     */
    private LocalDateTime updatedAt;

    /**
     * Количество карт, принадлежащих пользователю
     */
    private Integer cardsCount;

    /**
     * Список карт пользователя
     */
    private List<CardResponse> cards;
}