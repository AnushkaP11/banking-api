package com.bank.api.service;

import com.bank.api.dto.TransactionDTO;
import com.bank.api.exception.AccountStatusException;
import com.bank.api.exception.InsufficientBalanceException;
import com.bank.api.exception.ResourceNotFoundException;
import com.bank.api.exception.ValidationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for banking transaction operations.
 */
public interface TransactionService {

    /**
     * Deposits an amount into the specified account.
     *
     * @param accountId the target account ID
     * @param amount    the amount to deposit (must be &gt; 0)
     * @return a success message
     * @throws ResourceNotFoundException if the account does not exist
     * @throws ValidationException       if the amount is zero or negative
     * @throws AccountStatusException    if the account is not ACTIVE
     */
    String deposit(Long accountId, BigDecimal amount);

    /**
     * Withdraws an amount from the specified account.
     *
     * @param accountId the source account ID
     * @param amount    the amount to withdraw (must be &gt; 0)
     * @return a success message
     * @throws ResourceNotFoundException    if the account does not exist
     * @throws ValidationException          if the amount is zero or negative
     * @throws AccountStatusException       if the account is not ACTIVE
     * @throws InsufficientBalanceException if the account balance is insufficient
     */
    String withdraw(Long accountId, BigDecimal amount);

    /**
     * Transfers an amount from one account to another atomically.
     *
     * @param fromId the source account ID
     * @param toId   the destination account ID
     * @param amount the amount to transfer (must be &gt; 0)
     * @return a success message
     * @throws ResourceNotFoundException    if either account does not exist
     * @throws ValidationException          if amount is invalid or source equals destination
     * @throws AccountStatusException       if either account is not ACTIVE
     * @throws InsufficientBalanceException if the source account balance is insufficient
     */
    String transfer(Long fromId, Long toId, BigDecimal amount);

    /**
     * Returns paginated transaction history for an account with optional date-range filtering.
     *
     * @param accountId the account ID
     * @param startDate optional start of date range (inclusive)
     * @param endDate   optional end of date range (inclusive)
     * @param pageable  pagination and sorting parameters
     * @return a page of transaction DTOs
     */
    Page<TransactionDTO> getTransactions(Long accountId, LocalDateTime startDate,
                                         LocalDateTime endDate, Pageable pageable);

    /**
     * Returns the last 5 transactions for an account ordered by date descending.
     *
     * @param accountId the account ID
     * @return list of the 5 most recent transactions
     */
    List<TransactionDTO> getMiniStatement(Long accountId);
}