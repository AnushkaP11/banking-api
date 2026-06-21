package com.bank.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Account request and response payload")
public class AccountDTO {

    @Schema(description = "Unique account identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long accountId;

    @Schema(description = "Auto-generated 10-digit account number", example = "3001234567", accessMode = Schema.AccessMode.READ_ONLY)
    private String accountNumber;

    @Schema(description = "Current account balance", example = "1000.00")
    private BigDecimal balance;

    @Schema(description = "Account type: SAVINGS or CURRENT", example = "SAVINGS", allowableValues = {"SAVINGS", "CURRENT"})
    private String type;

    @Schema(description = "ID of the owning customer", example = "1")
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