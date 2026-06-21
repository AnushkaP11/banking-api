package com.bank.api.service.impl;

import com.bank.api.dto.AccountDTO;
import com.bank.api.exception.AccountClosureException;
import com.bank.api.exception.ResourceNotFoundException;
import com.bank.api.mapper.AccountMapper;
import com.bank.api.model.Account;
import com.bank.api.model.AccountStatus;
import com.bank.api.model.Customer;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.CustomerRepository;
import com.bank.api.service.AccountService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private static final long ACCOUNT_NUMBER_MIN = 1_000_000_000L;
    private static final long ACCOUNT_NUMBER_RANGE = 9_000_000_000L;
    private static final String ACCOUNT_NOT_FOUND = "Account not found";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountServiceImpl(AccountRepository accountRepository,
                              CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public AccountDTO createAccount(AccountDTO dto) {

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Account account = AccountMapper.toEntity(dto);

        account.setCustomer(customer);
        account.setAccountNumber(generateAccountNumber());
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(dto.getBalance() != null ? dto.getBalance() : BigDecimal.ZERO);

        return AccountMapper.toDTO(accountRepository.save(account));
    }

    @Override
    public AccountDTO getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND));

        return AccountMapper.toDTO(account);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::toDTO)
                .toList();
    }

    @Override
    public List<AccountDTO> getAccountsByCustomer(Long customerId) {

        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return accountRepository.findByCustomerCustomerId(customerId)
                .stream()
                .map(AccountMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void updateAccountStatus(Long id, String status) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND));

        AccountStatus newStatus = AccountStatus.valueOf(status.toUpperCase());

        if (newStatus == AccountStatus.CLOSED
                && account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new AccountClosureException(
                    "Cannot close account with a positive balance of " + account.getBalance()
                    + ". Please withdraw funds before closing.");
        }

        account.setStatus(newStatus);
        accountRepository.save(account);
    }

    @Override
    public BigDecimal getBalance(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND));

        return account.getBalance();
    }

    private String generateAccountNumber() {
        return String.valueOf(SECURE_RANDOM.nextLong(ACCOUNT_NUMBER_RANGE) + ACCOUNT_NUMBER_MIN);
    }
}
