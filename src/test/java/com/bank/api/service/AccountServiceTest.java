package com.bank.api.service;

import com.bank.api.dto.AccountDTO;
import com.bank.api.exception.AccountClosureException;
import com.bank.api.exception.ResourceNotFoundException;
import com.bank.api.model.Account;
import com.bank.api.model.AccountStatus;
import com.bank.api.model.AccountType;
import com.bank.api.model.Customer;
import com.bank.api.model.CustomerStatus;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.CustomerRepository;
import com.bank.api.service.impl.AccountServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountServiceImpl service;

    private Customer customer;
    private Account account;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setFullName("Alice Johnson");
        customer.setEmail("alice@bank.com");
        customer.setMobile("9876543210");
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setCreatedAt(LocalDateTime.now());

        account = new Account();
        account.setAccountId(1L);
        account.setAccountNumber("1000000001");
        account.setType(AccountType.SAVINGS);
        account.setBalance(new BigDecimal("5000.00"));
        account.setStatus(AccountStatus.ACTIVE);
        account.setCustomer(customer);
    }

    @Test
    void createAccount_success_returnsAccountWithGeneratedNumber() {
        AccountDTO dto = new AccountDTO();
        dto.setCustomerId(1L);
        dto.setType("SAVINGS");
        dto.setBalance(new BigDecimal("1000.00"));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDTO result = service.createAccount(dto);

        assertNotNull(result);
        assertNotNull(result.getAccountNumber());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_customerNotFound_throwsResourceNotFoundException() {
        AccountDTO dto = new AccountDTO();
        dto.setCustomerId(99L);

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.createAccount(dto));
    }

    @Test
    void getAccountById_found_returnsDTO() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AccountDTO result = service.getAccountById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getAccountId());
        assertEquals("SAVINGS", result.getType());
    }

    @Test
    void getAccountById_notFound_throwsResourceNotFoundException() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getAccountById(99L));
    }

    @Test
    void getAccountsByCustomer_returnsAccounts() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.findByCustomerCustomerId(1L)).thenReturn(List.of(account));

        List<AccountDTO> result = service.getAccountsByCustomer(1L);

        assertEquals(1, result.size());
    }

    @Test
    void updateAccountStatus_toActive_succeeds() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(account);

        service.updateAccountStatus(1L, "SUSPENDED");

        assertEquals(AccountStatus.SUSPENDED, account.getStatus());
    }

    @Test
    void updateAccountStatus_toClosed_withPositiveBalance_throwsAccountClosureException() {
        account.setBalance(new BigDecimal("100.00"));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(AccountClosureException.class, () -> service.updateAccountStatus(1L, "CLOSED"));
    }

    @Test
    void updateAccountStatus_toClosed_withZeroBalance_succeeds() {
        account.setBalance(BigDecimal.ZERO);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(account);

        service.updateAccountStatus(1L, "CLOSED");

        assertEquals(AccountStatus.CLOSED, account.getStatus());
    }

    @Test
    void getBalance_returnsCurrentBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        BigDecimal balance = service.getBalance(1L);

        assertEquals(new BigDecimal("5000.00"), balance);
    }
}
