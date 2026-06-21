package com.bank.api.mapper;

import com.bank.api.dto.TransactionDTO;
import com.bank.api.model.Transaction;

public class TransactionMapper {

    private TransactionMapper() {}

    public static TransactionDTO toDTO(Transaction txn) {

        TransactionDTO dto = new TransactionDTO();

        dto.setTxnId(txn.getTxnId());
        dto.setType(txn.getType().toString());
        dto.setAmount(txn.getAmount());
        dto.setDescription(txn.getDescription());
        dto.setTxnDate(txn.getTxnDate());

        return dto;
    }
}
