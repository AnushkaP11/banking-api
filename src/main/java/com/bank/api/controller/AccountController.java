package com.bank.api.controller;

import com.bank.api.model.Account;
import com.bank.api.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // ✅ CREATE ACCOUNT
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.openAccount(account);
    }

    // ✅ GET ACCOUNT
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    // ✅ GET ACCOUNTS BY CUSTOMER
    @GetMapping("/customer/{customerId}")
    public List<Account> getAccountsByCustomer(@PathVariable Long customerId) {
        return accountService.getAccountsByCustomerId(customerId);
    }

    // ✅ GET BALANCE
    @GetMapping("/{id}/balance")
    public BigDecimal getBalance(@PathVariable Long id) {
        return accountService.getAccountById(id).getBalance();
    }

    // ✅ ✅ FIXED PATCH STATUS (MAIN ISSUE FIXED HERE)
    @PatchMapping("/{id}/status")
    public Account changeStatus(@PathVariable Long id) {
        return accountService.changeAccountStatus(id);
    }
}
