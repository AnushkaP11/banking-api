package com.bank.api.service.impl;

import com.bank.api.model.Account;
import com.bank.api.model.AccountStatus;
import com.bank.api.repository.AccountRepository;
import com.bank.api.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account openAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Account> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findAll();
    }

    @Override
    public void changeAccountStatus(Long accountId) {
        Account account = getAccountById(accountId);
        account.setStatus(AccountStatus.SUSPENDED);
        accountRepository.save(account);
    }
}

