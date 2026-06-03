package com.bank.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private String accountNumber;  // ✅ PDF requirement

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // ✅ getters

    public Long getAccountId() { return accountId; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public AccountType getType() { return type; }
    public AccountStatus getStatus() { return status; }
    public Customer getCustomer() { return customer; }

    // ✅ setters

    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public void setType(AccountType type) { this.type = type; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}