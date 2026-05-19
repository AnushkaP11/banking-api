package com.bank.api.service;

import com.bank.api.model.Customer;
import java.util.List;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    Customer getCustomerById(Long id);

    List<Customer> getAllCustomers();

    void deactivateCustomer(Long id);

    // ✅ IMPORTANT (fixes your error)
    Customer updateCustomer(Customer customer);
}
