package com.bank.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account status update payload")
public class StatusDTO {

    @Schema(description = "New account status", example = "SUSPENDED",
            allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED", "CLOSED"})
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}