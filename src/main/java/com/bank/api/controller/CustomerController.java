package com.bank.api.controller;

import com.bank.api.model.Customer;
import com.bank.api.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    // ✅ CREATE CUSTOMER
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return service.createCustomer(customer);
    }

    // ✅ GET CUSTOMER BY ID
    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return service.getCustomerById(id);
    }

    // ✅ ✅ GET ALL CUSTOMERS WITH PAGINATION
    @GetMapping
    public Page<Customer> getAllCustomers(
            @RequestParam int page,
            @RequestParam int size) {

        return service.getAllCustomers(page, size);
    }

    // ✅ UPDATE CUSTOMER
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id,
                                   @RequestBody Customer updated) {
        return service.updateCustomer(id, updated);
    }

    // ✅ ✅ DELETE (RETURN ONLY STATUS)
    @DeleteMapping("/{id}")
    public Map<String, String> deleteCustomer(@PathVariable Long id) {

        Customer deleted = service.deleteCustomer(id);

        Map<String, String> response = new HashMap<>();
        response.put("status", deleted.getStatus().toString());

        return response;
    }
}