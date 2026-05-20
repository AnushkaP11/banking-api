package com.bank.api.service.impl;

import com.bank.api.model.Customer;
import com.bank.api.model.CustomerStatus;
import com.bank.api.repository.CustomerRepository;
import com.bank.api.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    // ✅ CREATE CUSTOMER
    @Override
    public Customer createCustomer(Customer customer) {
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setCreatedAt(LocalDateTime.now());
        return repository.save(customer);
    }

    // ✅ GET CUSTOMER BY ID
    @Override
    public Customer getCustomerById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // ✅ ✅ PAGINATION METHOD
    @Override
    public Page<Customer> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    // ✅ UPDATE CUSTOMER
    @Override
    public Customer updateCustomer(Long id, Customer updated) {
        Customer customer = getCustomerById(id);
        customer.setEmail(updated.getEmail());
        customer.setMobile(updated.getMobile());
        return repository.save(customer);
    }

    // ✅ DELETE (SOFT DELETE)
    @Override
    public Customer deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customer.setStatus(CustomerStatus.INACTIVE);
        return repository.save(customer);
    }
}
