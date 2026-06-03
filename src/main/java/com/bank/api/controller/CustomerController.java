package com.bank.api.controller;

import com.bank.api.dto.CustomerDTO;
import com.bank.api.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    // ✅ CREATE
    @PostMapping
    public CustomerDTO createCustomer(@Valid @RequestBody CustomerDTO dto) {
        return service.createCustomer(dto);
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) {
        return service.getCustomerById(id);
    }

    // ✅ PAGINATION
    @GetMapping
    public Page<CustomerDTO> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return service.getAllCustomers(pageable);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id,
                                      @Valid @RequestBody CustomerDTO dto) {
        return service.updateCustomer(id, dto);
    }

    // ✅ DELETE (SOFT DELETE)
    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        service.deleteCustomer(id);
        return "Customer set to INACTIVE";
    }
}
