package com.example.bankcards.service.impl;

import com.example.bankcards.config.EncryptionConfig;
import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.request.CardFilterRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.CardOperationException;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.spec.CardSpecification;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.CardNumberGenerator;
import com.example.bankcards.util.CardNumberMasker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserService userService;
    private final EncryptionConfig encryptionConfig;

    @Override
    @Transactional
    public CardResponse createCard(CardCreateRequest request, String username) {
        User user = userService.findByUsername(username);

        String cardNumber = generateUniqueCardNumber();

        Card card = Card.builder()
                .cardNumber(encryptionConfig.encrypt(cardNumber))
                .cardHolderName(request.getCardHolderName())
                .expirationDate(LocalDate.now().plusYears(3))
                .cvv(encryptionConfig.encrypt(generateCVV()))
                .currency(request.getCurrency())
                .type(request.getType())
                .user(user)
                .build();

        Card savedCard = cardRepository.save(card);
        log.info("Created new {} card for user: {}", request.getType(), username);

        return mapToResponse(savedCard);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponse> getUserCards(CardFilterRequest filter, String username, Pageable pageable) {
        User user = userService.findByUsername(username);
        Specification<Card> spec = CardSpecification.withFilters(user, filter);

        return cardRepository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CardResponse getCardDetails(Long cardId, String username) {
        User user = userService.findByUsername(username);
        Card card = cardRepository.findByIdAndUser(cardId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        return mapToResponse(card);
    }

    @Override
    @Transactional
    public CardResponse blockCard(Long cardId, String username) {
        User user = userService.findByUsername(username);
        Card card = cardRepository.findByIdAndUser(cardId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new CardOperationException("Card already blocked");
        }

        if (card.isExpired()) {
            throw new CardOperationException("Cannot block expired card");
        }

        card.setStatus(CardStatus.BLOCKED);
        Card updatedCard = cardRepository.save(card);
        log.info("Card {} blocked by user {}", cardId, username);

        return mapToResponse(updatedCard);
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId, String username) {
        User user = userService.findByUsername(username);
        Card card = cardRepository.findByIdAndUser(cardId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        // Проверяем, можно ли удалять карту
        if (card.getBalance().compareTo(java.math.BigDecimal.ZERO) > 0) {
            throw new CardOperationException("Cannot delete card with positive balance");
        }

        if (card.getStatus() == CardStatus.ACTIVE) {
            throw new CardOperationException("Cannot delete active card. Block it first.");
        }

        cardRepository.delete(card);
        log.info("Card {} deleted by user {}", cardId, username);
    }

    private CardResponse mapToResponse(Card card) {
        return CardResponse.builder()
                .id(card.getId())
                .cardNumber(CardNumberMasker.mask(encryptionConfig.decrypt(card.getCardNumber())))
                .cardHolderName(card.getCardHolderName())
                .expirationDate(card.getExpirationDate())
                .balance(card.getBalance())
                .currency(card.getCurrency())
                .status(card.getStatus())
                .createdAt(card.getCreatedAt())
                .type(card.getType() != null ? card.getType().name() : null)
                .build();
    }

    private String generateUniqueCardNumber() {
        String cardNumber;
        do {
            cardNumber = CardNumberGenerator.generate();
        } while (cardRepository.existsByCardNumber(encryptionConfig.encrypt(cardNumber)));

        return cardNumber;
    }

    private String generateCVV() {
        return String.format("%03d", (int) (Math.random() * 1000));
    }
}