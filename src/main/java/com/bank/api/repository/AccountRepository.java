package com.bank.api.repository;

import com.bank.api.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // ✅ VERY IMPORTANT METHOD
    List<Account> findByCustomerCustomerId(Long customerId);
}