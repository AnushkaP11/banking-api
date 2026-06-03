package com.bank.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txnId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    private String description;

    private LocalDateTime txnDate;

    @ManyToOne
    private Account account;

    // getters & setters

    public Long getTxnId() { return txnId; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getTxnDate() { return txnDate; }
    public Account getAccount() { return account; }

    public void setTxnId(Long txnId) { this.txnId = txnId; }
    public void setType(TransactionType type) { this.type = type; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setTxnDate(LocalDateTime txnDate) { this.txnDate = txnDate; }
    public void setAccount(Account account) { this.account = account; }
}