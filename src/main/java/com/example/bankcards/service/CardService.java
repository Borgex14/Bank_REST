package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.request.CardFilterRequest;
import com.example.bankcards.dto.response.CardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Сервис для управления банковскими картами.
 */
public interface CardService {

    /**
     * Создает новую банковскую карту.
     */
    CardResponse createCard(CardCreateRequest request, String username);

    /**
     * Получает список карт пользователя с фильтрацией и пагинацией.
     */
    Page<CardResponse> getUserCards(CardFilterRequest filter, String username, Pageable pageable);

    /**
     * Блокирует карту пользователя.
     */
    CardResponse blockCard(Long cardId, String username);

    /**
     * Получает детальную информацию о карте.
     */
    CardResponse getCardDetails(Long cardId, String username);

    /**
     * Удаляет карту пользователя.
     */
    void deleteCard(Long cardId, String username);
}