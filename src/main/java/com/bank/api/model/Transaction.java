package com.bank.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txnId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;
    private String description;
    private LocalDateTime txnDate;

    // ✅ FINAL FIX
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnoreProperties({"customer", "hibernateLazyInitializer", "handler"})
    private Account account;

    public Transaction() {}

    public Transaction(TransactionType type,
                       BigDecimal amount,
                       String description,
                       Account account) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.account = account;
        this.txnDate = LocalDateTime.now();
    }

    // ✅ Getters (IMPORTANT)
    public Long getTxnId() { return txnId; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getTxnDate() { return txnDate; }
}