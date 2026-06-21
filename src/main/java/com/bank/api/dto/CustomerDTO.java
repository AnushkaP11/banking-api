package com.bank.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Schema(description = "Customer request and response payload")
public class CustomerDTO {

    @Schema(description = "Unique customer identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long customerId;

    @Schema(description = "Full name of the customer", example = "John Doe")
    @NotBlank(message = "Full name required")
    private String fullName;

    @Schema(description = "Customer email address (must be unique)", example = "john.doe@example.com")
    @Email(message = "Invalid email")
    private String email;

    @Schema(description = "10-digit mobile number", example = "9876543210")
    @NotBlank
    @Pattern(regexp = "^\\d{10}$", message = "Mobile must be 10 digits")
    private String mobile;

    @Schema(description = "Customer status", example = "ACTIVE", accessMode = Schema.AccessMode.READ_ONLY)
    private String status;

    @Schema(description = "Account creation timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    // getters & setters

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
