package com.example.bankcards.service;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;

/**
 * Сервис для управления переводами между картами.
 */
public interface TransferService {

    /**
     * Выполняет перевод между картами одного пользователя.
     */
    TransferResponse transferBetweenOwnCards(TransferRequest request, String username);

    /**
     * Получает детальную информацию о переводе.
     */
    TransferResponse getTransferDetails(Long transferId, String username);
}