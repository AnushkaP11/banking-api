package com.bank.api.controller;

import com.bank.api.service.TransactionService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    // ✅ DEPOSIT
    @PostMapping("/transactions/deposit")
    public String deposit(@RequestParam Long accountId,
                          @RequestParam Double amount) {

        return service.deposit(accountId, amount);
    }

    // ✅ WITHDRAW
    @PostMapping("/transactions/withdraw")
    public String withdraw(@RequestParam Long accountId,
                           @RequestParam Double amount) {

        return service.withdraw(accountId, amount);
    }

    // ✅ TRANSFER
    @PostMapping("/transactions/transfer")
    public String transfer(@RequestParam Long fromId,
                           @RequestParam Long toId,
                           @RequestParam Double amount) {

        return service.transfer(fromId, toId, amount);
    }

    // ✅ HISTORY
    @GetMapping("/accounts/{id}/transactions")
    public List<?> getTransactions(@PathVariable Long id) {
        return service.getTransactions(id);
    }

    // ✅ MINI STATEMENT
    @GetMapping("/accounts/{id}/ministatement")
    public List<?> getMiniStatement(@PathVariable Long id) {
        return service.getMiniStatement(id);
    }
}