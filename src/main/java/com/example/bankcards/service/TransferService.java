package com.example.bankcards.service;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;

public interface TransferService {
    TransferResponse transferBetweenOwnCards(TransferRequest request, String username);
    TransferResponse getTransferDetails(Long transferId, String username);
}