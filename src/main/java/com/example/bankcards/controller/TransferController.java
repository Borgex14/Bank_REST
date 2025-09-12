package com.example.bankcards.controller;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TransferController {

    private final TransferService transferService;
    private final SecurityUtils securityUtils;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Transfer between own cards")
    public ResponseEntity<TransferResponse> transfer(
            @Valid @RequestBody TransferRequest request) {
        String username = securityUtils.getCurrentUsername();
        return ResponseEntity.ok(transferService.transferBetweenOwnCards(request, username));
    }

    @GetMapping("/{transferId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get transfer details")
    public ResponseEntity<TransferResponse> getTransferDetails(@PathVariable Long transferId) {
        String username = securityUtils.getCurrentUsername();
        return ResponseEntity.ok(transferService.getTransferDetails(transferId, username));
    }
}