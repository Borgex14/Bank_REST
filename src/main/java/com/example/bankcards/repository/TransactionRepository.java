package com.example.bankcards.repository;

import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromCardId(Long fromCardId);

    List<Transaction> findByToCardId(Long toCardId);

    @Query("SELECT t FROM Transaction t WHERE t.fromCard.user = :user OR t.toCard.user = :user")
    List<Transaction> findByUser(@Param("user") User user);

    @Query("SELECT t FROM Transaction t WHERE t.fromCard.user = :user OR t.toCard.user = :user")
    Page<Transaction> findByUser(@Param("user") User user, Pageable pageable);

    List<Transaction> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM Transaction t WHERE t.id = :transactionId AND (t.fromCard.user = :user OR t.toCard.user = :user)")
    Optional<Transaction> findByIdAndUser(@Param("transactionId") Long transactionId, @Param("user") User user);
}