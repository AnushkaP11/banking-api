package com.bank.api.service;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    String deposit(Long accountId, BigDecimal amount);

    String withdraw(Long accountId, BigDecimal amount);

    String transfer(Long fromId, Long toId, BigDecimal amount);

    List<?> getTransactions(Long accountId);

    List<?> getMiniStatement(Long accountId);
}