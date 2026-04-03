package com.app.qma_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ConvertRequest {

    @NotNull(message = "Value is required")
    private Double value;

    @NotBlank(message = "From unit is required")
    private String fromUnit;

    @NotBlank(message = "To unit is required")
    private String toUnit;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getFromUnit() {
        return fromUnit;
    }

    public void setFromUnit(String fromUnit) {
        this.fromUnit = fromUnit;
    }

    public String getToUnit() {
        return toUnit;
    }

    public void setToUnit(String toUnit) {
        this.toUnit = toUnit;
    }
}
