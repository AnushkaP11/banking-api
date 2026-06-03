package com.bank.api.mapper;

import com.bank.api.dto.CustomerDTO;
import com.bank.api.model.Customer;

public class CustomerMapper {

    public static CustomerDTO toDTO(Customer customer) {

        CustomerDTO dto = new CustomerDTO();

        dto.setCustomerId(customer.getCustomerId());
        dto.setFullName(customer.getFullName());
        dto.setEmail(customer.getEmail());
        dto.setMobile(customer.getMobile());

        if (customer.getStatus() != null) {
            dto.setStatus(customer.getStatus().name());
        }

        dto.setCreatedAt(customer.getCreatedAt());

        return dto;
    }

    public static Customer toEntity(CustomerDTO dto) {

        Customer customer = new Customer();

        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail());
        customer.setMobile(dto.getMobile());

        return customer;
    }
}