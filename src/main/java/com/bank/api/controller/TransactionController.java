package com.bank.api.controller;

import com.bank.api.dto.TransactionDTO;
import com.bank.api.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Transaction", description = "APIs for deposits, withdrawals, transfers, and transaction history")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @Operation(summary = "Deposit funds", description = "Deposits the specified amount into an account")
    @ApiResponse(responseCode = "200", description = "Deposit successful")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @ApiResponse(responseCode = "400", description = "Amount must be greater than zero")
    @ApiResponse(responseCode = "409", description = "Account is not active")
    @PostMapping("/transactions/deposit")
    public String deposit(
            @Parameter(description = "Account ID to deposit into", required = true) @RequestParam Long accountId,
            @Parameter(description = "Amount to deposit", required = true) @RequestParam BigDecimal amount) {
        return service.deposit(accountId, amount);
    }

    @Operation(summary = "Withdraw funds", description = "Withdraws the specified amount from an account")
    @ApiResponse(responseCode = "200", description = "Withdrawal successful")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @ApiResponse(responseCode = "400", description = "Amount must be greater than zero")
    @ApiResponse(responseCode = "409", description = "Account is not active")
    @ApiResponse(responseCode = "422", description = "Insufficient balance")
    @PostMapping("/transactions/withdraw")
    public String withdraw(
            @Parameter(description = "Account ID to withdraw from", required = true) @RequestParam Long accountId,
            @Parameter(description = "Amount to withdraw", required = true) @RequestParam BigDecimal amount) {
        return service.withdraw(accountId, amount);
    }

    @Operation(summary = "Transfer funds", description = "Transfers the specified amount from one account to another")
    @ApiResponse(responseCode = "200", description = "Transfer successful")
    @ApiResponse(responseCode = "404", description = "Source or destination account not found")
    @ApiResponse(responseCode = "400", description = "Same account or invalid amount")
    @ApiResponse(responseCode = "409", description = "Account is not active")
    @ApiResponse(responseCode = "422", description = "Insufficient balance")
    @PostMapping("/transactions/transfer")
    public String transfer(
            @Parameter(description = "Source account ID", required = true) @RequestParam Long fromId,
            @Parameter(description = "Destination account ID", required = true) @RequestParam Long toId,
            @Parameter(description = "Amount to transfer", required = true) @RequestParam BigDecimal amount) {
        return service.transfer(fromId, toId, amount);
    }

    @Operation(summary = "Get transaction history",
            description = "Returns paginated transaction history for an account with optional date-range filter")
    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/accounts/{id}/transactions")
    public Page<TransactionDTO> getTransactions(
            @Parameter(description = "Account ID", required = true) @PathVariable Long id,
            @Parameter(description = "Filter from date (ISO 8601)") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Filter to date (ISO 8601)") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "txnDate") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));
        return service.getTransactions(id, startDate, endDate, pageable);
    }

    @Operation(summary = "Get mini statement", description = "Returns the last 5 transactions for an account")
    @ApiResponse(responseCode = "200", description = "Mini statement retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/accounts/{id}/mini-statement")
    public List<TransactionDTO> getMiniStatement(
            @Parameter(description = "Account ID", required = true) @PathVariable Long id) {
        return service.getMiniStatement(id);
    }
}

