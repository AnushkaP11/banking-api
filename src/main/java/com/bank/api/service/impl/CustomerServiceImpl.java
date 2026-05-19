package com.bank.api.service.impl;

import com.bank.api.model.Customer;
import com.bank.api.model.CustomerStatus;
import com.bank.api.repository.CustomerRepository;
import com.bank.api.service.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // ✅ CREATE
    @Override
    public Customer createCustomer(Customer customer) {

        if (customer.getStatus() == null) {
            customer.setStatus(CustomerStatus.ACTIVE);
        }

        if (customer.getCreatedAt() == null) {
            customer.setCreatedAt(LocalDateTime.now());
        }

        return customerRepository.save(customer);
    }

    // ✅ GET BY ID
    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    // ✅ GET ALL
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // ✅ DELETE (SOFT DELETE)
    @Override
    public void deactivateCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customer.setStatus(CustomerStatus.INACTIVE);
        customerRepository.save(customer);
    }

    // ✅ UPDATE (FINAL FIX)
    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}