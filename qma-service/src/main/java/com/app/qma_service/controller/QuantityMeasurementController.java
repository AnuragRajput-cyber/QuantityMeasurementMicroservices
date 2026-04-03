package com.app.qma_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.qma_service.dto.ConvertRequest;
import com.app.qma_service.dto.QuantityDTO;
import com.app.qma_service.service.IQuantityMeasurementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quantity")
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public QuantityDTO add(@Valid @RequestBody QuantityDTO[] quantities) {
        validatePair(quantities);
        return service.addQuantity(quantities[0], quantities[1]);
    }

    @PostMapping("/compare")
    public boolean compare(@Valid @RequestBody QuantityDTO[] quantities) {
        validatePair(quantities);
        return service.compareQuantity(quantities[0], quantities[1]);
    }

    @PostMapping("/subtract")
    public QuantityDTO subtract(@Valid @RequestBody QuantityDTO[] quantities) {
        validatePair(quantities);
        return service.subQuantity(quantities[0], quantities[1]);
    }

    @PostMapping("/divide")
    public double divide(@Valid @RequestBody QuantityDTO[] quantities) {
        validatePair(quantities);
        return service.divQuantity(quantities[0], quantities[1]);
    }

    @PostMapping("/convert")
    public QuantityDTO convert(@Valid @RequestBody ConvertRequest request) {
        QuantityDTO input = new QuantityDTO(request.getValue(), request.getFromUnit());
        return service.convert(input, request.getToUnit());
    }

    private void validatePair(QuantityDTO[] quantities) {
        if (quantities == null || quantities.length != 2) {
            throw new IllegalArgumentException("Exactly two quantities are required");
        }
    }
}
