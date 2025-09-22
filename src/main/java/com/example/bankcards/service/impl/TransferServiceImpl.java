package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.enums.TransactionStatus;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Реализация сервиса для управления переводами между картами.
 * Обеспечивает выполнение переводов и получение информации о них.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {

    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    /**
     * Выполняет перевод денежных средств между картами одного пользователя.
     *
     * @param request данные для перевода
     * @param username имя пользователя, выполняющего перевод
     * @return TransferResponse с информацией о выполненном переводе
     * @throws ResourceNotFoundException если карты не найдены или не принадлежат пользователю
     * @throws IllegalArgumentException если параметры перевода некорректны
     * @throws InsufficientFundsException если на карте-отправителе недостаточно средств
     */
    @Override
    @Transactional
    public TransferResponse transferBetweenOwnCards(TransferRequest request, String username) {
        var user = userService.findByUsername(username);

        Card fromCard = cardRepository.findByIdAndUser(request.getFromCardId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Source card not found"));

        Card toCard = cardRepository.findByIdAndUser(request.getToCardId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Target card not found"));

        validateTransfer(fromCard, toCard, request.getAmount());

        fromCard.setBalance(fromCard.getBalance().subtract(request.getAmount()));
        toCard.setBalance(toCard.getBalance().add(request.getAmount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        Transaction transaction = Transaction.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(TransactionStatus.COMPLETED)
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transfer completed from card {} to card {}, amount: {}",
                request.getFromCardId(), request.getToCardId(), request.getAmount());

        return mapToResponse(savedTransaction);
    }

    /**
     * Получает детальную информацию о конкретном переводе пользователя.
     *
     * @param transferId идентификатор перевода
     * @param username имя пользователя
     * @return TransferResponse с информацией о переводе
     * @throws ResourceNotFoundException если перевод не найден или не принадлежит пользователю
     */
    @Override
    public TransferResponse getTransferDetails(Long transferId, String username) {
        var user = userService.findByUsername(username);
        Transaction transaction = transactionRepository.findByIdAndUser(transferId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));

        return mapToResponse(transaction);
    }

    /**
     * Проверяет корректность параметров перевода.
     *
     * @param fromCard карта-отправитель
     * @param toCard карта-получатель
     * @param amount сумма перевода
     * @throws IllegalArgumentException если параметры перевода некорректны
     * @throws InsufficientFundsException если недостаточно средств на карте-отправителе
     */
    private void validateTransfer(Card fromCard, Card toCard, BigDecimal amount) {
        if (fromCard.equals(toCard)) {
            throw new IllegalArgumentException("Cannot transfer to the same card");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        if (!fromCard.getCurrency().equals(toCard.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }

        if (fromCard.isBlocked() || toCard.isBlocked()) {
            throw new IllegalArgumentException("Card is blocked");
        }

        if (fromCard.isExpired() || toCard.isExpired()) {
            throw new IllegalArgumentException("Card is expired");
        }
    }

    /**
     * Преобразует сущность Transaction в TransferResponse.
     *
     * @param transaction сущность транзакции
     * @return TransferResponse с информацией о переводе
     */
    private TransferResponse mapToResponse(Transaction transaction) {
        return TransferResponse.builder()
                .id(transaction.getId())
                .fromCardId(transaction.getFromCard().getId())
                .toCardId(transaction.getToCard().getId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .timestamp(transaction.getTimestamp())
                .build();
    }
}