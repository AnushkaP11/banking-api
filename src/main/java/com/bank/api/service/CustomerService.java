package com.bank.api.service;

import com.bank.api.dto.CustomerDTO;
import com.bank.api.exception.DuplicateResourceException;
import com.bank.api.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for customer lifecycle management.
 */
public interface CustomerService {

    /**
     * Registers a new customer in the system.
     *
     * @param dto the customer data including name, email, and mobile
     * @return the created customer with assigned ID and timestamps
     * @throws DuplicateResourceException if a customer with the same email already exists
     */
    CustomerDTO createCustomer(CustomerDTO dto);

    /**
     * Retrieves a customer by their unique identifier.
     *
     * @param id the customer ID
     * @return the customer details
     * @throws ResourceNotFoundException if no customer exists with the given ID
     */
    CustomerDTO getCustomerById(Long id);

    /**
     * Returns a paginated list of all active and inactive customers.
     *
     * @param pageable pagination and sorting parameters
     * @return a page of customer DTOs
     */
    Page<CustomerDTO> getAllCustomers(Pageable pageable);

    /**
     * Updates the email and mobile number of an existing customer.
     *
     * @param id  the customer ID
     * @param dto the updated customer data
     * @return the updated customer details
     * @throws ResourceNotFoundException  if no customer exists with the given ID
     * @throws DuplicateResourceException if the new email is already in use by another customer
     */
    CustomerDTO updateCustomer(Long id, CustomerDTO dto);

    /**
     * Soft-deletes a customer by setting their status to INACTIVE.
     *
     * @param id the customer ID
     * @throws ResourceNotFoundException if no customer exists with the given ID
     */
    void deleteCustomer(Long id);
}