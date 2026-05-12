package com.bank.api.service.impl;

import com.bank.api.model.Account;
import com.bank.api.model.AccountStatus;
import com.bank.api.model.Transaction;
import com.bank.api.model.TransactionType;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.TransactionRepository;
import com.bank.api.service.TransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public Transaction deposit(Long accountId, BigDecimal amount) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // ✅ RULE 1: Amount must be > 0
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        // ✅ RULE 2: Account must be active
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active");
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction txn = new Transaction(
                TransactionType.CREDIT,
                amount,
                "Deposit",
                account
        );

        return transactionRepository.save(txn);
    }

    // ✅ WITHDRAW
    @Override
    public Transaction withdraw(Long accountId, BigDecimal amount) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // ✅ RULE 1: Amount must be > 0
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        // ✅ RULE 2: Account must be active
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active");
        }

        // ✅ RULE 3: Sufficient balance check
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction txn = new Transaction(
                TransactionType.DEBIT,
                amount,
                "Withdraw",
                account
        );

        return transactionRepository.save(txn);
    }

    // ✅ TRANSFER
    @Override
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        // ✅ RULE 1: Accounts must be different
        if (fromAccountId.equals(toAccountId)) {
            throw new RuntimeException("Source and destination accounts cannot be same");
        }

        // ✅ RULE 2: Amount must be > 0
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        // ✅ RULE 3: Both accounts must be ACTIVE
        if (fromAccount.getStatus() != AccountStatus.ACTIVE ||
                toAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("One of the accounts is not active");
        }

        // ✅ RULE 4: Check sufficient balance
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // ✅ Perform transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // ✅ Record transactions
        Transaction debitTxn = new Transaction(
                TransactionType.TRANSFER,
                amount,
                "Transfer to Account " + toAccountId,
                fromAccount
        );

        Transaction creditTxn = new Transaction(
                TransactionType.TRANSFER,
                amount,
                "Transfer from Account " + fromAccountId,
                toAccount
        );

        transactionRepository.save(debitTxn);
        transactionRepository.save(creditTxn);
    }

    // ✅ MINI STATEMENT
    @Override
    public List<Transaction> getMiniStatement(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return transactionRepository.findTop5ByAccountOrderByTxnDateDesc(account);
    }
}