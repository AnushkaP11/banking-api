package com.bank.api.repository;

import com.bank.api.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountAccountId(Long accountId);

    List<Transaction> findTop5ByAccountAccountIdOrderByTxnDateDesc(Long accountId);
}