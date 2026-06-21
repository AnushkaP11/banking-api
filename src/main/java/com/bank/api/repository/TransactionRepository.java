package com.bank.api.repository;

import com.bank.api.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountAccountId(Long accountId);

    List<Transaction> findTop5ByAccountAccountIdOrderByTxnDateDesc(Long accountId);

    Page<Transaction> findByAccountAccountId(Long accountId, Pageable pageable);

    Page<Transaction> findByAccountAccountIdAndTxnDateBetween(
            Long accountId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}