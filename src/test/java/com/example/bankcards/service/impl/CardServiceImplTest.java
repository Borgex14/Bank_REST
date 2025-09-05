package com.example.bankcards.service.impl;

import com.example.bankcards.config.EncryptionConfig;
import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserService userService;

    @Mock
    private EncryptionConfig encryptionConfig;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void shouldCreateCardSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(Role.USER);

        CardCreateRequest request = new CardCreateRequest();
        request.setCardHolderName("John Doe");
        request.setCurrency("USD");
        request.setType(CardType.DEBIT);

        when(userService.findByUsername("testuser")).thenReturn(user);
        when(encryptionConfig.encrypt(any())).thenReturn("encrypted");
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card card = invocation.getArgument(0);
            card.setId(1L);
            return card;
        });

        var response = cardService.createCard(request, "testuser");

        assertNotNull(response);
        assertEquals("John Doe", response.getCardHolderName());
        assertEquals("USD", response.getCurrency());
        assertEquals(CardStatus.ACTIVE, response.getStatus());
        verify(cardRepository, times(1)).save(any(Card.class));
    }
}