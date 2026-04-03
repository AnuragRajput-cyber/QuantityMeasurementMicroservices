package com.app.qma_service.service;

import com.app.qma_service.dto.QuantityDTO;

public interface IQuantityMeasurementService {

    QuantityDTO addQuantity(QuantityDTO q1, QuantityDTO q2);

    boolean compareQuantity(QuantityDTO q1, QuantityDTO q2);

    QuantityDTO subQuantity(QuantityDTO q1, QuantityDTO q2);

    double divQuantity(QuantityDTO q1, QuantityDTO q2);

    QuantityDTO convert(QuantityDTO quantity, String toUnit);
}
