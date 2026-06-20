package com.bank.api.service.impl;

import com.bank.api.exception.AccountStatusException;
import com.bank.api.exception.InsufficientBalanceException;
import com.bank.api.exception.ResourceNotFoundException;
import com.bank.api.exception.ValidationException;
import com.bank.api.model.Account;
import com.bank.api.model.AccountStatus;
import com.bank.api.model.Transaction;
import com.bank.api.model.TransactionType;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.TransactionRepository;
import com.bank.api.service.TransactionService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public String deposit(Long accountId, BigDecimal amount) {
        validatePositiveAmount(amount);

        Account account = findActiveAccount(accountId);

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        saveTransaction(account, amount, TransactionType.CREDIT, "Deposit");

        return "Amount deposited successfully";
    }

    @Override
    @Transactional
    public String withdraw(Long accountId, BigDecimal amount) {
        validatePositiveAmount(amount);

        Account account = findActiveAccount(accountId);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: " + account.getBalance() + ", Requested: " + amount);
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        saveTransaction(account, amount, TransactionType.DEBIT, "Withdrawal");

        return "Amount withdrawn successfully";
    }

    @Override
    @Transactional
    public String transfer(Long fromId, Long toId, BigDecimal amount) {
        validatePositiveAmount(amount);

        if (fromId.equals(toId)) {
            throw new ValidationException("Source and destination accounts must be different");
        }

        Account from = findActiveAccount(fromId);
        Account to = findActiveAccount(toId);

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance in source account. Available: " + from.getBalance() + ", Requested: " + amount);
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountRepository.save(from);
        accountRepository.save(to);

        saveTransaction(from, amount, TransactionType.DEBIT, "Transfer to account " + to.getAccountNumber());
        saveTransaction(to, amount, TransactionType.CREDIT, "Transfer from account " + from.getAccountNumber());

        return "Transfer successful";
    }

    @Override
    public List<Transaction> getTransactions(Long accountId) {
        return transactionRepository.findByAccountAccountId(accountId);
    }

    @Override
    public List<Transaction> getMiniStatement(Long accountId) {
        return transactionRepository.findTop5ByAccountAccountIdOrderByTxnDateDesc(accountId);
    }

    private Account findActiveAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountStatusException(
                    "Transactions are not allowed on a " + account.getStatus() + " account");
        }

        return account;
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be greater than zero");
        }
    }

    private void saveTransaction(Account account, BigDecimal amount,
                                 TransactionType type, String description) {
        Transaction txn = new Transaction();
        txn.setAmount(amount);
        txn.setType(type);
        txn.setDescription(description);
        txn.setTxnDate(LocalDateTime.now());
        txn.setAccount(account);
        transactionRepository.save(txn);
    }
}
