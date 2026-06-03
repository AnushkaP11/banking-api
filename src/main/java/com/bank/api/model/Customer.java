package com.bank.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String fullName;
    private String email;
    private String mobile;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    // getters

    public Long getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public CustomerStatus getStatus() { return status; }

    // setters

    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setStatus(CustomerStatus status) { this.status = status; }
}