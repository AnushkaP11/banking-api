package com.bank.api.service;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    String deposit(Long accountId, Double amount);

    String withdraw(Long accountId, Double amount);

    String transfer(Long fromId, Long toId, Double amount);

    List<?> getTransactions(Long accountId);

    List<?> getMiniStatement(Long accountId);
}