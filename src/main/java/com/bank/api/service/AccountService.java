package com.bank.api.service;

import com.bank.api.dto.AccountDTO;
import java.util.List;
import java.math.BigDecimal;

public interface AccountService {

    AccountDTO createAccount(AccountDTO dto);

    AccountDTO getAccountById(Long id);

    List<AccountDTO> getAccountsByCustomer(Long customerId);

    List<AccountDTO> getAllAccounts();

    void updateAccountStatus(Long id, String status);

    // ✅ ADD THIS (BALANCE API)
    BigDecimal getBalance(Long id);
}