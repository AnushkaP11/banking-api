package com.bank.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, unique = true, length = 10)
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    // getters

    public Long getAccountId() { return accountId; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public AccountType getType() { return type; }
    public AccountStatus getStatus() { return status; }
    public Customer getCustomer() { return customer; }
    public List<Transaction> getTransactions() { return transactions; }

    // setters

    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public void setType(AccountType type) { this.type = type; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
}