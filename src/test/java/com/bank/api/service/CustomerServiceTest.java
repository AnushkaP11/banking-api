package com.bank.api.service;

import com.bank.api.dto.CustomerDTO;
import com.bank.api.exception.DuplicateResourceException;
import com.bank.api.exception.ResourceNotFoundException;
import com.bank.api.model.Customer;
import com.bank.api.model.CustomerStatus;
import com.bank.api.repository.CustomerRepository;
import com.bank.api.service.impl.CustomerServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;

    private Customer customer;
    private CustomerDTO dto;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setFullName("Alice Johnson");
        customer.setEmail("alice@bank.com");
        customer.setMobile("9876543210");
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setCreatedAt(LocalDateTime.now());

        dto = new CustomerDTO();
        dto.setFullName("Alice Johnson");
        dto.setEmail("alice@bank.com");
        dto.setMobile("9876543210");
    }

    @Test
    void createCustomer_success() {
        when(repository.findByEmail("alice@bank.com")).thenReturn(Optional.empty());
        when(repository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = service.createCustomer(dto);

        assertNotNull(result);
        assertEquals("Alice Johnson", result.getFullName());
        verify(repository).save(any(Customer.class));
    }

    @Test
    void createCustomer_duplicateEmail_throwsDuplicateResourceException() {
        when(repository.findByEmail("alice@bank.com")).thenReturn(Optional.of(customer));

        assertThrows(DuplicateResourceException.class, () -> service.createCustomer(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void getCustomerById_found_returnsDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDTO result = service.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCustomerId());
    }

    @Test
    void getCustomerById_notFound_throwsResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getCustomerById(99L));
    }

    @Test
    void getAllCustomers_returnsPaginatedResults() {
        Page<Customer> page = new PageImpl<>(List.of(customer));
        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<CustomerDTO> result = service.getAllCustomers(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateCustomer_success_updatesEmailAndMobile() {
        CustomerDTO update = new CustomerDTO();
        update.setEmail("newalice@bank.com");
        update.setMobile("9000000000");

        when(repository.findById(1L)).thenReturn(Optional.of(customer));
        when(repository.findByEmail("newalice@bank.com")).thenReturn(Optional.empty());
        when(repository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = service.updateCustomer(1L, update);

        assertNotNull(result);
        verify(repository).save(any(Customer.class));
    }

    @Test
    void updateCustomer_duplicateEmail_throwsDuplicateResourceException() {
        Customer other = new Customer();
        other.setCustomerId(2L);
        other.setEmail("newalice@bank.com");

        CustomerDTO update = new CustomerDTO();
        update.setEmail("newalice@bank.com");
        update.setMobile("9000000000");

        when(repository.findById(1L)).thenReturn(Optional.of(customer));
        when(repository.findByEmail("newalice@bank.com")).thenReturn(Optional.of(other));

        assertThrows(DuplicateResourceException.class, () -> service.updateCustomer(1L, update));
    }

    @Test
    void deleteCustomer_setsStatusInactive() {
        when(repository.findById(1L)).thenReturn(Optional.of(customer));
        when(repository.save(any(Customer.class))).thenReturn(customer);

        service.deleteCustomer(1L);

        assertEquals(CustomerStatus.INACTIVE, customer.getStatus());
        verify(repository).save(customer);
    }

    @Test
    void deleteCustomer_notFound_throwsResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCustomer(99L));
    }
}
