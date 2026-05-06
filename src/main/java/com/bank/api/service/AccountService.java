package com.bank.api.service;

import com.bank.api.model.Account;

import java.util.List;

public interface AccountService {

    Account openAccount(Account account);

    Account getAccountById(Long id);

    List<Account> getAccountsByCustomerId(Long customerId);

    void changeAccountStatus(Long accountId);
}