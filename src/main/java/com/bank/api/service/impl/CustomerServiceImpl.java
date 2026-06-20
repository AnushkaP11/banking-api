package com.bank.api.service.impl;

import com.bank.api.dto.CustomerDTO;
import com.bank.api.exception.DuplicateResourceException;
import com.bank.api.exception.ResourceNotFoundException;
import com.bank.api.mapper.CustomerMapper;
import com.bank.api.model.Customer;
import com.bank.api.model.CustomerStatus;
import com.bank.api.repository.CustomerRepository;
import com.bank.api.service.CustomerService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO dto) {

        repository.findByEmail(dto.getEmail()).ifPresent(existing -> {
            throw new DuplicateResourceException(
                    "A customer with email '" + dto.getEmail() + "' already exists");
        });

        Customer customer = CustomerMapper.toEntity(dto);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setStatus(CustomerStatus.ACTIVE);

        return CustomerMapper.toDTO(repository.save(customer));
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        return CustomerMapper.toDTO(customer);
    }

    @Override
    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        return repository.findAll(pageable)
                .map(CustomerMapper::toDTO);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        if (!customer.getEmail().equals(dto.getEmail())) {
            repository.findByEmail(dto.getEmail()).ifPresent(existing -> {
                throw new DuplicateResourceException(
                        "A customer with email '" + dto.getEmail() + "' already exists");
            });
        }

        customer.setEmail(dto.getEmail());
        customer.setMobile(dto.getMobile());

        return CustomerMapper.toDTO(repository.save(customer));
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setStatus(CustomerStatus.INACTIVE);
        repository.save(customer);
    }
}
