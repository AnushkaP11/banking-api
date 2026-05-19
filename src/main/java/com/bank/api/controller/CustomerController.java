package com.bank.api.controller;

import com.bank.api.model.Customer;
import com.bank.api.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    // ✅ Constructor
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // ✅ CREATE CUSTOMER
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    // ✅ GET CUSTOMER BY ID
    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    // ✅ GET ALL CUSTOMERS
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // ✅ UPDATE CUSTOMER ✅
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id,
                                   @RequestBody Customer updated) {

        Customer customer = customerService.getCustomerById(id);

        customer.setEmail(updated.getEmail());
        customer.setMobile(updated.getMobile());

        return customerService.updateCustomer(customer);
    }

    // ✅ DELETE CUSTOMER (SOFT DELETE)
    @DeleteMapping("/{id}")
    public void deactivateCustomer(@PathVariable Long id) {
        customerService.deactivateCustomer(id);
    }
}