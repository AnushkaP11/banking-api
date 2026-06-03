package com.bank.api.service.impl;

import com.bank.api.dto.AccountDTO;
import com.bank.api.exception.ResourceNotFoundException;
import com.bank.api.mapper.AccountMapper;
import com.bank.api.model.Account;
import com.bank.api.model.AccountStatus;
import com.bank.api.model.Customer;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.CustomerRepository;
import com.bank.api.service.AccountService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountServiceImpl(AccountRepository accountRepository,
                              CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    // ✅ CREATE
    @Override
    public AccountDTO createAccount(AccountDTO dto) {

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Account account = AccountMapper.toEntity(dto);

        account.setCustomer(customer);
        account.setAccountNumber(generateAccountNumber());
        account.setStatus(AccountStatus.ACTIVE);

        return AccountMapper.toDTO(accountRepository.save(account));
    }

    // ✅ GET ACCOUNT
    @Override
    public AccountDTO getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return AccountMapper.toDTO(account);
    }

    // ✅ GET ALL
    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::toDTO)
                .toList();
    }

    // ✅ GET BY CUSTOMER
    @Override
    public List<AccountDTO> getAccountsByCustomer(Long customerId) {

        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return accountRepository.findByCustomerCustomerId(customerId)
                .stream()
                .map(AccountMapper::toDTO)
                .toList();
    }

    // ✅ CHANGE STATUS
    @Override
    public void updateAccountStatus(Long id, String status) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setStatus(AccountStatus.valueOf(status));

        accountRepository.save(account);
    }

    // ✅ ✅ ✅ BALANCE API (MAIN FIX)
    @Override
    public BigDecimal getBalance(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return account.getBalance();
    }

    // ✅ ACCOUNT NUMBER GENERATOR
    private String generateAccountNumber() {
        return String.valueOf((long)(Math.random() * 9000000000L) + 1000000000L);
    }
}