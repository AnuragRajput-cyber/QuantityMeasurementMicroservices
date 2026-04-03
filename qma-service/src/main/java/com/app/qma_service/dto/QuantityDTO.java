package com.app.qma_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuantityDTO {

    @NotNull(message = "Value is required")
    private Double value;

    @NotBlank(message = "Unit is required")
    private String unit;

    public QuantityDTO() {
    }

    public QuantityDTO(Double value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
