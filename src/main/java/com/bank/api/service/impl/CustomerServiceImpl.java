package com.bank.api.service.impl;

import com.bank.api.dto.CustomerDTO;
import com.bank.api.exception.ResourceNotFoundException;
import com.bank.api.mapper.CustomerMapper;
import com.bank.api.model.Customer;
import com.bank.api.model.CustomerStatus;
import com.bank.api.repository.CustomerRepository;
import com.bank.api.service.CustomerService;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    // ✅ CREATE
    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) {

        Customer customer = CustomerMapper.toEntity(dto);

        customer.setCreatedAt(LocalDateTime.now());
        customer.setStatus(CustomerStatus.ACTIVE);

        return CustomerMapper.toDTO(repository.save(customer));
    }

    // ✅ GET BY ID
    @Override
    public CustomerDTO getCustomerById(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return CustomerMapper.toDTO(customer);
    }

    // ✅ PAGINATION
    @Override
    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        return repository.findAll(pageable)
                .map(CustomerMapper::toDTO);
    }

    // ✅ UPDATE
    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setEmail(dto.getEmail());
        customer.setMobile(dto.getMobile());

        return CustomerMapper.toDTO(repository.save(customer));
    }

    // ✅ SOFT DELETE
    @Override
    public void deleteCustomer(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setStatus(CustomerStatus.INACTIVE);

        repository.save(customer);
    }
}