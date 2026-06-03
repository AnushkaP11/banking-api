package com.bank.api.dto;

import java.math.BigDecimal;

public class AccountDTO {

    private Long accountId;
    private String accountNumber;   // ✅ add this
    private BigDecimal balance;
    private String type;
    private Long customerId;

    // getters

    public Long getAccountId() { return accountId; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public String getType() { return type; }
    public Long getCustomerId() { return customerId; }

    // setters

    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public void setType(String type) { this.type = type; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}