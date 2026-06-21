package com.bank.api.service;

import com.bank.api.dto.AccountDTO;
import com.bank.api.exception.AccountClosureException;
import com.bank.api.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for bank account management operations.
 */
public interface AccountService {

    /**
     * Creates a new bank account for an existing customer.
     *
     * @param dto the account data including customer ID and account type
     * @return the created account with auto-generated account number
     * @throws ResourceNotFoundException if the customer does not exist
     */
    AccountDTO createAccount(AccountDTO dto);

    /**
     * Retrieves an account by its unique identifier.
     *
     * @param id the account ID
     * @return the account details including current balance
     * @throws ResourceNotFoundException if no account exists with the given ID
     */
    AccountDTO getAccountById(Long id);

    /**
     * Returns all accounts belonging to a specific customer.
     *
     * @param customerId the customer ID
     * @return list of accounts owned by the customer
     * @throws ResourceNotFoundException if the customer does not exist
     */
    List<AccountDTO> getAccountsByCustomer(Long customerId);

    /**
     * Returns all accounts in the system.
     *
     * @return list of all accounts
     */
    List<AccountDTO> getAllAccounts();

    /**
     * Updates the status of an account (ACTIVE, SUSPENDED, CLOSED, INACTIVE).
     *
     * @param id     the account ID
     * @param status the new status value
     * @throws ResourceNotFoundException if no account exists with the given ID
     * @throws AccountClosureException   if closing an account that still has a positive balance
     */
    void updateAccountStatus(Long id, String status);

    /**
     * Returns the current balance of an account.
     *
     * @param id the account ID
     * @return current balance as BigDecimal
     * @throws ResourceNotFoundException if no account exists with the given ID
     */
    BigDecimal getBalance(Long id);
}