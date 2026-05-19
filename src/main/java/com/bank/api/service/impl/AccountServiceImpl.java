package com.bank.api.service.impl;

import com.bank.api.model.Account;
import com.bank.api.model.AccountStatus;
import com.bank.api.model.Customer;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.CustomerRepository;
import com.bank.api.service.AccountService;
import org.springframework.stereotype.Service;

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

    // ✅ CREATE ACCOUNT
    @Override
    public Account openAccount(Account account) {

        // ✅ ensure status always set
        account.setStatus(AccountStatus.ACTIVE);

        Long customerId = account.getCustomer().getCustomerId();

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        account.setCustomer(customer);

        return accountRepository.save(account);
    }

    // ✅ GET ACCOUNT
    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    // ✅ GET ACCOUNTS (simple version)
    @Override
    public List<Account> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findAll();
    }

    // ✅ ✅ FINAL FIXED STATUS METHOD (no 500 error)
    @Override
    public Account changeAccountStatus(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        // ✅ safe toggle
        if (account.getStatus() == null || account.getStatus() == AccountStatus.SUSPENDED) {
            account.setStatus(AccountStatus.ACTIVE);
        } else {
            account.setStatus(AccountStatus.SUSPENDED);
        }

        return accountRepository.save(account);
    }
}
