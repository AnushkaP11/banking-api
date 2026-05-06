package com.bank.api.service;

import com.bank.api.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    Transaction deposit(Long accountId, BigDecimal amount);

    Transaction withdraw(Long accountId, BigDecimal amount);

    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);

    List<Transaction> getMiniStatement(Long accountId);
}

