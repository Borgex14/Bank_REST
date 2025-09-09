package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.TransactionStatus;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @InjectMocks
    private TransferServiceImpl transferService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    private User testUser;
    private Card fromCard;
    private Card toCard;
    private TransferRequest transferRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        fromCard = new Card();
        fromCard.setId(1L);
        fromCard.setUser(testUser);
        fromCard.setBalance(new BigDecimal("1000.00"));
        fromCard.setCurrency("USD");
        fromCard.setStatus(CardStatus.ACTIVE);
        fromCard.setExpirationDate(LocalDate.now().plusYears(1));

        toCard = new Card();
        toCard.setId(2L);
        toCard.setUser(testUser);
        toCard.setBalance(new BigDecimal("500.00"));
        toCard.setCurrency("USD");
        toCard.setStatus(CardStatus.ACTIVE);
        toCard.setExpirationDate(LocalDate.now().plusYears(1));

        transferRequest = new TransferRequest();
        transferRequest.setFromCardId(1L);
        transferRequest.setToCardId(2L);
        transferRequest.setAmount(new BigDecimal("100.00"));
        transferRequest.setCurrency("USD");
        transferRequest.setDescription("Test transfer");
    }

    @Test
    void transferBetweenOwnCards_ValidRequest_ReturnsResponse() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(cardRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdAndUser(2L, testUser)).thenReturn(Optional.of(toCard));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction transaction = Transaction.builder()
                .id(1L)
                .fromCard(fromCard)
                .toCard(toCard)
                .amount(transferRequest.getAmount())
                .currency(transferRequest.getCurrency())
                .status(TransactionStatus.COMPLETED)
                .description(transferRequest.getDescription())
                .timestamp(LocalDateTime.now())
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        TransferResponse response = transferService.transferBetweenOwnCards(transferRequest, "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getFromCardId());
        assertEquals(2L, response.getToCardId());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
        assertEquals(TransactionStatus.COMPLETED, response.getStatus());

        verify(cardRepository, times(2)).save(any(Card.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void transferBetweenOwnCards_SourceCardNotFound_ThrowsException() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(cardRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            transferService.transferBetweenOwnCards(transferRequest, "testuser");
        });
    }

    @Test
    void transferBetweenOwnCards_InsufficientFunds_ThrowsException() {
        // Arrange
        transferRequest.setAmount(new BigDecimal("2000.00"));
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(cardRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdAndUser(2L, testUser)).thenReturn(Optional.of(toCard));

        // Act & Assert
        assertThrows(InsufficientFundsException.class, () -> {
            transferService.transferBetweenOwnCards(transferRequest, "testuser");
        });
    }

    @Test
    void transferBetweenOwnCards_SameCard_ThrowsException() {
        // Arrange
        transferRequest.setToCardId(1L); // Same as fromCard
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(cardRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(fromCard));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            transferService.transferBetweenOwnCards(transferRequest, "testuser");
        });
    }

    @Test
    void transferBetweenOwnCards_CurrencyMismatch_ThrowsException() {
        // Arrange
        toCard.setCurrency("EUR");
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(cardRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdAndUser(2L, testUser)).thenReturn(Optional.of(toCard));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            transferService.transferBetweenOwnCards(transferRequest, "testuser");
        });
    }

    @Test
    void transferBetweenOwnCards_CardBlocked_ThrowsException() {
        // Arrange
        fromCard.setStatus(CardStatus.BLOCKED);
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(cardRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdAndUser(2L, testUser)).thenReturn(Optional.of(toCard));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            transferService.transferBetweenOwnCards(transferRequest, "testuser");
        });
    }

    @Test
    void getTransferDetails_ValidTransfer_ReturnsResponse() {
        // Arrange
        Transaction transaction = Transaction.builder()
                .id(1L)
                .fromCard(fromCard)
                .toCard(toCard)
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .status(TransactionStatus.COMPLETED)
                .description("Test transfer")
                .timestamp(LocalDateTime.now())
                .build();

        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(transactionRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(transaction));

        // Act
        TransferResponse response = transferService.getTransferDetails(1L, "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getFromCardId());
        assertEquals(2L, response.getToCardId());
    }

    @Test
    void getTransferDetails_TransferNotFound_ThrowsException() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(transactionRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            transferService.getTransferDetails(1L, "testuser");
        });
    }
}