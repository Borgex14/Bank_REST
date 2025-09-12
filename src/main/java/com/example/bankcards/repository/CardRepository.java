package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {

    Optional<Card> findByIdAndUser(Long id, User user);

    boolean existsByCardNumber(String encryptedCardNumber);

    List<Card> findByUser(User user);

    Page<Card> findByUser(User user, Pageable pageable);

    List<Card> findByUserAndStatus(User user, CardStatus status);

    @Query("SELECT c FROM Card c WHERE c.user = :user AND c.expirationDate < CURRENT_DATE")
    List<Card> findExpiredCardsByUser(@Param("user") User user);

    @Query("SELECT c FROM Card c WHERE c.user = :user AND c.balance > 0")
    List<Card> findCardsWithPositiveBalance(@Param("user") User user);
}