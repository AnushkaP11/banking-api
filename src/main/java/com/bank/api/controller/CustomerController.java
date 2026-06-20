package com.bank.api.controller;

import com.bank.api.dto.CustomerDTO;
import com.bank.api.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer", description = "APIs for managing bank customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new customer", description = "Registers a new customer in the banking system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error in request data")
    })
    @PostMapping
    public CustomerDTO createCustomer(@Valid @RequestBody CustomerDTO dto) {
        return service.createCustomer(dto);
    }

    @Operation(summary = "Get customer by ID", description = "Retrieves a customer's details by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public CustomerDTO getCustomer(
            @Parameter(description = "Customer ID", required = true) @PathVariable Long id) {
        return service.getCustomerById(id);
    }

    @Operation(summary = "Get all customers", description = "Returns a paginated list of all customers")
    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    @GetMapping
    public Page<CustomerDTO> getAllCustomers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page") @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.getAllCustomers(pageable);
    }

    @Operation(summary = "Update customer", description = "Updates an existing customer's information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "400", description = "Validation error in request data")
    })
    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(
            @Parameter(description = "Customer ID", required = true) @PathVariable Long id,
            @Valid @RequestBody CustomerDTO dto) {
        return service.updateCustomer(id, dto);
    }

    @Operation(summary = "Delete customer", description = "Soft-deletes a customer by setting their status to INACTIVE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer set to INACTIVE"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public String deleteCustomer(
            @Parameter(description = "Customer ID", required = true) @PathVariable Long id) {
        service.deleteCustomer(id);
        return "Customer set to INACTIVE";
    }
}
