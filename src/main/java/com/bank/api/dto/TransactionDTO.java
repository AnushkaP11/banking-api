package com.bank.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Transaction response payload")
public class TransactionDTO {

    @Schema(description = "Unique transaction identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long txnId;

    @Schema(description = "Transaction amount", example = "500.00")
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Schema(description = "Transaction type: CREDIT, DEBIT, or TRANSFER", example = "CREDIT",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String type;

    @Schema(description = "Transaction description", example = "Deposit", accessMode = Schema.AccessMode.READ_ONLY)
    private String description;

    @Schema(description = "Date and time of the transaction", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime txnDate;

    public Long getTxnId() { return txnId; }
    public void setTxnId(Long txnId) { this.txnId = txnId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getTxnDate() { return txnDate; }
    public void setTxnDate(LocalDateTime txnDate) { this.txnDate = txnDate; }
}
