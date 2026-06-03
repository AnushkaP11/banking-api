package com.bank.api.service.impl;

import com.bank.api.exception.ResourceNotFoundException;
import com.bank.api.model.*;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.TransactionRepository;
import com.bank.api.service.TransactionService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountRepository accountRepository,
                                  TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    // ✅ DEPOSIT
    @Override
    public String deposit(Long accountId, Double amount) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }

        BigDecimal amt = BigDecimal.valueOf(amount);
        account.setBalance(account.getBalance().add(amt));

        accountRepository.save(account);

        // ✅ Save transaction
        Transaction txn = new Transaction();
        txn.setAmount(amt);
        txn.setType(TransactionType.CREDIT);
        txn.setDescription("Deposit");
        txn.setTxnDate(LocalDateTime.now());
        txn.setAccount(account);

        transactionRepository.save(txn);

        return "Amount deposited successfully";
    }

    // ✅ WITHDRAW
    @Override
    public String withdraw(Long accountId, Double amount) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        BigDecimal amt = BigDecimal.valueOf(amount);

        if (account.getBalance().compareTo(amt) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amt));

        accountRepository.save(account);

        Transaction txn = new Transaction();
        txn.setAmount(amt);
        txn.setType(TransactionType.DEBIT);
        txn.setDescription("Withdraw");
        txn.setTxnDate(LocalDateTime.now());
        txn.setAccount(account);

        transactionRepository.save(txn);

        return "Amount withdrawn successfully";
    }

    // ✅ TRANSFER
    @Override
    public String transfer(Long fromId, Long toId, Double amount) {

        if (fromId.equals(toId)) {
            throw new RuntimeException("Same account transfer not allowed");
        }

        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new ResourceNotFoundException("Source not found"));

        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found"));

        BigDecimal amt = BigDecimal.valueOf(amount);

        if (from.getBalance().compareTo(amt) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        from.setBalance(from.getBalance().subtract(amt));
        to.setBalance(to.getBalance().add(amt));

        accountRepository.save(from);
        accountRepository.save(to);

        return "Transfer successful";
    }

    // ✅ HISTORY
    @Override
    public List<Transaction> getTransactions(Long accountId) {
        return transactionRepository.findByAccountAccountId(accountId);
    }

    // ✅ MINI STATEMENT
    @Override
    public List<Transaction> getMiniStatement(Long accountId) {

        List<Transaction> list = transactionRepository.findByAccountAccountId(accountId);

        return list.stream()
                .sorted((a, b) -> b.getTxnDate().compareTo(a.getTxnDate()))
                .limit(5)
                .toList();
    }
}