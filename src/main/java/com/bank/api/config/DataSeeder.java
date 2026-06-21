package com.bank.api.config;

import com.bank.api.model.Account;
import com.bank.api.model.AccountStatus;
import com.bank.api.model.AccountType;
import com.bank.api.model.Customer;
import com.bank.api.model.CustomerStatus;
import com.bank.api.model.Transaction;
import com.bank.api.model.TransactionType;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.CustomerRepository;
import com.bank.api.repository.TransactionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Seeds demo data into the in-memory H2 database on application startup.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    private static final String INITIAL_DEPOSIT = "Initial deposit";

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DataSeeder(CustomerRepository customerRepository,
                      AccountRepository accountRepository,
                      TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {
        if (customerRepository.count() > 0) {
            logger.info("Demo data already present — skipping seed.");
            return;
        }

        logger.info("Seeding demo data...");

        Customer alice = createCustomer("Alice Johnson", "alice@bank.com", "9876543210");
        Customer bob = createCustomer("Bob Smith", "bob@bank.com", "9123456780");
        Customer carol = createCustomer("Carol White", "carol@bank.com", "9988776655");

        Account aliceSavings = createAccount("1000000001", AccountType.SAVINGS, new BigDecimal("5000.00"), alice);
        Account aliceCurrent = createAccount("1000000002", AccountType.CURRENT, new BigDecimal("2000.00"), alice);
        Account bobSavings = createAccount("1000000003", AccountType.SAVINGS, new BigDecimal("8000.00"), bob);
        Account carolSavings = createAccount("1000000004", AccountType.SAVINGS, new BigDecimal("1500.00"), carol);

        createTransaction(aliceSavings, TransactionType.CREDIT, new BigDecimal("5000.00"), INITIAL_DEPOSIT, LocalDateTime.now().minusDays(10));
        createTransaction(aliceSavings, TransactionType.DEBIT, new BigDecimal("500.00"), "ATM withdrawal", LocalDateTime.now().minusDays(5));
        createTransaction(aliceSavings, TransactionType.CREDIT, new BigDecimal("200.00"), "Salary credit", LocalDateTime.now().minusDays(2));
        createTransaction(aliceCurrent, TransactionType.CREDIT, new BigDecimal("2000.00"), INITIAL_DEPOSIT, LocalDateTime.now().minusDays(8));
        createTransaction(bobSavings, TransactionType.CREDIT, new BigDecimal("8000.00"), INITIAL_DEPOSIT, LocalDateTime.now().minusDays(7));
        createTransaction(bobSavings, TransactionType.DEBIT, new BigDecimal("1000.00"), "Bill payment", LocalDateTime.now().minusDays(3));
        createTransaction(carolSavings, TransactionType.CREDIT, new BigDecimal("1500.00"), INITIAL_DEPOSIT, LocalDateTime.now().minusDays(6));

        logger.info("Demo data seeded: 3 customers, 4 accounts, 7 transactions.");
    }

    private Customer createCustomer(String fullName, String email, String mobile) {
        Customer customer = new Customer();
        customer.setFullName(fullName);
        customer.setEmail(email);
        customer.setMobile(mobile);
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setCreatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    private Account createAccount(String accountNumber, AccountType type, BigDecimal balance, Customer customer) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setType(type);
        account.setBalance(balance);
        account.setStatus(AccountStatus.ACTIVE);
        account.setCustomer(customer);
        return accountRepository.save(account);
    }

    private void createTransaction(Account account, TransactionType type,
                                   BigDecimal amount, String description, LocalDateTime txnDate) {
        Transaction txn = new Transaction();
        txn.setAccount(account);
        txn.setType(type);
        txn.setAmount(amount);
        txn.setDescription(description);
        txn.setTxnDate(txnDate);
        transactionRepository.save(txn);
    }
}
