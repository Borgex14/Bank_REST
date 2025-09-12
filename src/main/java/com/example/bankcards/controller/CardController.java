package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.request.CardFilterRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CardController {

    private final CardService cardService;
    private final SecurityUtils securityUtils;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new card", description = "Admin only endpoint")
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardCreateRequest request) {
        String username = securityUtils.getCurrentUsername();
        CardResponse response = cardService.createCard(request, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get user cards with filtering and pagination")
    public ResponseEntity<Page<CardResponse>> getUserCards(
            @ModelAttribute CardFilterRequest filter,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        String username = securityUtils.getCurrentUsername();
        Page<CardResponse> cards = cardService.getUserCards(filter, username, pageable);
        return ResponseEntity.ok(cards);
    }

    @PatchMapping("/{cardId}/block")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Block card")
    public ResponseEntity<CardResponse> blockCard(@PathVariable Long cardId) {
        String username = securityUtils.getCurrentUsername();
        CardResponse response = cardService.blockCard(cardId, username);
        return ResponseEntity.ok(response);
    }
}