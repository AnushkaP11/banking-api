package com.bank.api.mapper;

import com.bank.api.dto.AccountDTO;
import com.bank.api.model.Account;
import com.bank.api.model.AccountType;

public class AccountMapper {

    public static AccountDTO toDTO(Account account) {

        AccountDTO dto = new AccountDTO();

        dto.setAccountId(account.getAccountId());
        dto.setAccountNumber(account.getAccountNumber()); // ✅ important
        dto.setBalance(account.getBalance());

        if (account.getType() != null) {
            dto.setType(account.getType().name());
        }

        if (account.getCustomer() != null) {
            dto.setCustomerId(account.getCustomer().getCustomerId());
        }

        return dto;
    }

    public static Account toEntity(AccountDTO dto) {

        Account account = new Account();

        account.setBalance(dto.getBalance());

        if (dto.getType() != null) {
            account.setType(AccountType.valueOf(dto.getType()));
        }

        return account;
    }
}