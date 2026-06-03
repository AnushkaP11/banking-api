package com.bank.api.exception;

public class AccountStatusException extends RuntimeException {

    public AccountStatusException(String msg) {
        super(msg);
    }
}