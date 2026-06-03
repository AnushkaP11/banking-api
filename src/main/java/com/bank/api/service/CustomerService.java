package com.bank.api.service;

import com.bank.api.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    CustomerDTO createCustomer(CustomerDTO dto);

    CustomerDTO getCustomerById(Long id);

    Page<CustomerDTO> getAllCustomers(Pageable pageable);

    CustomerDTO updateCustomer(Long id, CustomerDTO dto);

    void deleteCustomer(Long id);
}