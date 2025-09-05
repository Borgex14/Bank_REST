package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.request.CardFilterRequest;
import com.example.bankcards.dto.response.CardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {
    CardResponse createCard(CardCreateRequest request, String username);
    Page<CardResponse> getUserCards(CardFilterRequest filter, String username, Pageable pageable);
    CardResponse blockCard(Long cardId, String username);
    CardResponse getCardDetails(Long cardId, String username);
    void deleteCard(Long cardId, String username);
}