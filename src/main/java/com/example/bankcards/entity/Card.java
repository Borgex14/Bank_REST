package com.example.bankcards.entity;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;
import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Convert(converter = CryptoConverter.class)
    @Column(name = "card_number", unique = true, nullable = false, length = 255)
    private String cardNumber;

    @Column(name = "card_holder_name", nullable = false, length = 100)
    private String cardHolderName;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Convert(converter = CryptoConverter.class)
    @Column(length = 255)
    private String cvv;

    @Column(precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CardStatus status = CardStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", length = 20)
    private CardType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "fromCard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> outgoingTransactions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "toCard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> incomingTransactions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (expirationDate == null) {
            expirationDate = LocalDate.now().plusYears(3);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }
}