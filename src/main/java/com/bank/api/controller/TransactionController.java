package com.bank.api.controller;

import com.bank.api.model.Transaction;
import com.bank.api.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public Transaction deposit(@RequestParam Long accountId,
                               @RequestParam BigDecimal amount) {
        return transactionService.deposit(accountId, amount);
    }

    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestParam Long accountId,
                                @RequestParam BigDecimal amount) {
        return transactionService.withdraw(accountId, amount);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestParam Long fromAccountId,
                         @RequestParam Long toAccountId,
                         @RequestParam BigDecimal amount) {
        transactionService.transfer(fromAccountId, toAccountId, amount);
    }

    @GetMapping("/statement/{accountId}")
    public List<Transaction> getMiniStatement(@PathVariable Long accountId) {
        return transactionService.getMiniStatement(accountId);
    }
}

