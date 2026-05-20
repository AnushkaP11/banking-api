package com.bank.api.service;

import com.bank.api.model.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    Customer getCustomerById(Long id);

    // ✅ Pagination
    Page<Customer> getAllCustomers(int page, int size);

    Customer updateCustomer(Long id, Customer updated);

    Customer deleteCustomer(Long id);
}
