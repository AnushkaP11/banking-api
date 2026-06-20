package com.bank.api.controller;

import com.bank.api.dto.AccountDTO;
import com.bank.api.dto.StatusDTO;
import com.bank.api.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Account", description = "APIs for managing bank accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new account", description = "Creates a new bank account for a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/accounts")
    public AccountDTO createAccount(@RequestBody AccountDTO dto) {
        return service.createAccount(dto);
    }

    @Operation(summary = "Get account by ID", description = "Retrieves a bank account by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(
            @Parameter(description = "Account ID", required = true) @PathVariable Long id) {
        return service.getAccountById(id);
    }

    @Operation(summary = "Get accounts by customer", description = "Retrieves all accounts belonging to a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/customers/{id}/accounts")
    public List<AccountDTO> getAccountsByCustomer(
            @Parameter(description = "Customer ID", required = true) @PathVariable Long id) {
        return service.getAccountsByCustomer(id);
    }

    @Operation(summary = "Update account status", description = "Changes the status of an account (e.g. ACTIVE, INACTIVE, CLOSED)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PatchMapping("/accounts/{id}/status")
    public String updateStatus(
            @Parameter(description = "Account ID", required = true) @PathVariable Long id,
            @RequestBody StatusDTO dto) {
        service.updateAccountStatus(id, dto.getStatus());
        return "Account status updated";
    }

    @Operation(summary = "Get account balance", description = "Returns the current balance of an account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/accounts/{id}/balance")
    public Map<String, Object> getBalance(
            @Parameter(description = "Account ID", required = true) @PathVariable Long id) {
        return Map.of(
                "accountId", id,
                "balance", service.getBalance(id)
        );
    }
}