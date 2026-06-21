package com.bank.api.service;

import com.bank.api.dto.TransactionDTO;
import com.bank.api.exception.AccountStatusException;
import com.bank.api.exception.InsufficientBalanceException;
import com.bank.api.exception.ResourceNotFoundException;
import com.bank.api.exception.ValidationException;
import com.bank.api.model.Account;
import com.bank.api.model.AccountStatus;
import com.bank.api.model.AccountType;
import com.bank.api.model.Customer;
import com.bank.api.model.Transaction;
import com.bank.api.model.TransactionType;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.TransactionRepository;
import com.bank.api.service.impl.TransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl service;

    private Account activeAccount;
    private Account suspendedAccount;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        activeAccount = new Account();
        activeAccount.setAccountId(1L);
        activeAccount.setAccountNumber("1000000001");
        activeAccount.setType(AccountType.SAVINGS);
        activeAccount.setBalance(new BigDecimal("1000.00"));
        activeAccount.setStatus(AccountStatus.ACTIVE);
        activeAccount.setCustomer(customer);

        suspendedAccount = new Account();
        suspendedAccount.setAccountId(2L);
        suspendedAccount.setAccountNumber("1000000002");
        suspendedAccount.setType(AccountType.SAVINGS);
        suspendedAccount.setBalance(new BigDecimal("500.00"));
        suspendedAccount.setStatus(AccountStatus.SUSPENDED);
        suspendedAccount.setCustomer(customer);
    }

    // --- DEPOSIT TESTS ---

    @Test
    void deposit_success_updatesBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(activeAccount));
        when(accountRepository.save(any())).thenReturn(activeAccount);

        String result = service.deposit(1L, new BigDecimal("500.00"));

        assertEquals("Amount deposited successfully", result);
        assertEquals(new BigDecimal("1500.00"), activeAccount.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void deposit_zeroAmount_throwsValidationException() {
        assertThrows(ValidationException.class, () -> service.deposit(1L, BigDecimal.ZERO));
        verify(accountRepository, never()).findById(any());
    }

    @Test
    void deposit_negativeAmount_throwsValidationException() {
        BigDecimal negativeAmount = new BigDecimal("-100");
        assertThrows(ValidationException.class, () -> service.deposit(1L, negativeAmount));
    }

    @Test
    void deposit_accountNotFound_throwsResourceNotFoundException() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());
        BigDecimal amount = new BigDecimal("100.00");
        assertThrows(ResourceNotFoundException.class, () -> service.deposit(99L, amount));
    }

    @Test
    void deposit_suspendedAccount_throwsAccountStatusException() {
        when(accountRepository.findById(2L)).thenReturn(Optional.of(suspendedAccount));
        BigDecimal amount = new BigDecimal("100.00");
        assertThrows(AccountStatusException.class, () -> service.deposit(2L, amount));
    }

    // --- WITHDRAW TESTS ---

    @Test
    void withdraw_success_updatesBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(activeAccount));
        when(accountRepository.save(any())).thenReturn(activeAccount);

        String result = service.withdraw(1L, new BigDecimal("400.00"));

        assertEquals("Amount withdrawn successfully", result);
        assertEquals(new BigDecimal("600.00"), activeAccount.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void withdraw_insufficientBalance_throwsInsufficientBalanceException() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(activeAccount));
        BigDecimal excessiveAmount = new BigDecimal("2000.00");
        assertThrows(InsufficientBalanceException.class,
                () -> service.withdraw(1L, excessiveAmount));
    }

    @Test
    void withdraw_zeroAmount_throwsValidationException() {
        assertThrows(ValidationException.class, () -> service.withdraw(1L, BigDecimal.ZERO));
    }

    @Test
    void withdraw_suspendedAccount_throwsAccountStatusException() {
        when(accountRepository.findById(2L)).thenReturn(Optional.of(suspendedAccount));
        BigDecimal amount = new BigDecimal("100.00");
        assertThrows(AccountStatusException.class, () -> service.withdraw(2L, amount));
    }

    // --- TRANSFER TESTS ---

    @Test
    void transfer_success_updatesBalances() {
        Account destination = new Account();
        destination.setAccountId(3L);
        destination.setAccountNumber("1000000003");
        destination.setBalance(new BigDecimal("200.00"));
        destination.setStatus(AccountStatus.ACTIVE);
        destination.setCustomer(activeAccount.getCustomer());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(activeAccount));
        when(accountRepository.findById(3L)).thenReturn(Optional.of(destination));

        String result = service.transfer(1L, 3L, new BigDecimal("300.00"));

        assertEquals("Transfer successful", result);
        assertEquals(new BigDecimal("700.00"), activeAccount.getBalance());
        assertEquals(new BigDecimal("500.00"), destination.getBalance());
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    void transfer_sameAccount_throwsValidationException() {
        BigDecimal amount = new BigDecimal("100.00");
        assertThrows(ValidationException.class,
                () -> service.transfer(1L, 1L, amount));
        verify(accountRepository, never()).findById(any());
    }

    @Test
    void transfer_insufficientBalance_throwsInsufficientBalanceException() {
        Account destination = new Account();
        destination.setAccountId(3L);
        destination.setBalance(BigDecimal.ZERO);
        destination.setStatus(AccountStatus.ACTIVE);
        destination.setCustomer(activeAccount.getCustomer());

        when(accountRepository.findById(1L)).thenReturn(Optional.of(activeAccount));
        when(accountRepository.findById(3L)).thenReturn(Optional.of(destination));
        BigDecimal excessiveAmount = new BigDecimal("5000.00");
        assertThrows(InsufficientBalanceException.class,
                () -> service.transfer(1L, 3L, excessiveAmount));
    }

    // --- TRANSACTION HISTORY TESTS ---

    @Test
    void getTransactions_noDateRange_returnsPaginatedResults() {
        Transaction txn = new Transaction();
        txn.setTxnId(1L);
        txn.setType(TransactionType.CREDIT);
        txn.setAmount(new BigDecimal("500.00"));
        txn.setTxnDate(LocalDateTime.now());
        txn.setAccount(activeAccount);

        Page<Transaction> page = new PageImpl<>(List.of(txn));
        when(transactionRepository.findByAccountAccountId(eq(1L), any(PageRequest.class))).thenReturn(page);

        Page<TransactionDTO> result = service.getTransactions(1L, null, null, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getMiniStatement_returnsUpToFiveTransactions() {
        Transaction txn = new Transaction();
        txn.setTxnId(1L);
        txn.setType(TransactionType.CREDIT);
        txn.setAmount(new BigDecimal("100.00"));
        txn.setTxnDate(LocalDateTime.now());
        txn.setAccount(activeAccount);

        when(transactionRepository.findTop5ByAccountAccountIdOrderByTxnDateDesc(1L)).thenReturn(List.of(txn));

        List<TransactionDTO> result = service.getMiniStatement(1L);

        assertEquals(1, result.size());
    }
}
