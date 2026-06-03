package com.bank.api.controller;

import com.bank.api.dto.AccountDTO;
import com.bank.api.dto.StatusDTO;
import com.bank.api.service.AccountService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    // ✅ CREATE
    @PostMapping("/accounts")
    public AccountDTO createAccount(@RequestBody AccountDTO dto) {
        return service.createAccount(dto);
    }

    // ✅ GET ACCOUNT
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return service.getAccountById(id);
    }

    // ✅ GET BY CUSTOMER
    @GetMapping("/customers/{id}/accounts")
    public List<AccountDTO> getAccountsByCustomer(@PathVariable Long id) {
        return service.getAccountsByCustomer(id);
    }

    // ✅ CHANGE STATUS
    @PatchMapping("/accounts/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestBody StatusDTO dto) {

        service.updateAccountStatus(id, dto.getStatus());
        return "Account status updated";
    }

    // ✅ ✅ ✅ BALANCE API (FINAL FIX)
    @GetMapping("/accounts/{id}/balance")
    public Map<String, Object> getBalance(@PathVariable Long id) {

        return Map.of(
                "accountId", id,
                "balance", service.getBalance(id)
        );
    }
}