package com.app.qma_service.service;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.app.qma_service.dto.QuantityDTO;
import com.app.qma_service.enums.LengthUnit;
import com.app.qma_service.enums.TemperatureUnit;
import com.app.qma_service.enums.VolumeUnit;
import com.app.qma_service.enums.WeightUnit;
import com.app.qma_service.exception.InvalidUnitException;
import com.app.qma_service.model.Quantity;
import com.app.qma_service.model.Unit;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    @Override
    public QuantityDTO addQuantity(QuantityDTO q1, QuantityDTO q2) {
        Unit u1 = resolveUnit(q1.getUnit());
        Unit u2 = resolveUnit(q2.getUnit());
        validateSameCategory(u1, u2);

        Quantity<Unit> quantity1 = new Quantity<>(q1.getValue(), u1);
        Quantity<Unit> quantity2 = new Quantity<>(q2.getValue(), u2);
        Quantity<Unit> result = quantity1.add(quantity2);

        return new QuantityDTO(result.getValue(), result.getUnit().toString());
    }

    @Override
    public boolean compareQuantity(QuantityDTO q1, QuantityDTO q2) {
        Unit u1 = resolveUnit(q1.getUnit());
        Unit u2 = resolveUnit(q2.getUnit());
        validateSameCategory(u1, u2);

        Quantity<Unit> quantity1 = new Quantity<>(q1.getValue(), u1);
        Quantity<Unit> quantity2 = new Quantity<>(q2.getValue(), u2);
        return quantity1.equals(quantity2);
    }

    @Override
    public QuantityDTO subQuantity(QuantityDTO q1, QuantityDTO q2) {
        Unit u1 = resolveUnit(q1.getUnit());
        Unit u2 = resolveUnit(q2.getUnit());
        validateSameCategory(u1, u2);

        Quantity<Unit> quantity1 = new Quantity<>(q1.getValue(), u1);
        Quantity<Unit> quantity2 = new Quantity<>(q2.getValue(), u2);
        Quantity<Unit> result = quantity1.subtract(quantity2);

        return new QuantityDTO(result.getValue(), result.getUnit().toString());
    }

    @Override
    public double divQuantity(QuantityDTO q1, QuantityDTO q2) {
        Unit u1 = resolveUnit(q1.getUnit());
        Unit u2 = resolveUnit(q2.getUnit());
        validateSameCategory(u1, u2);

        Quantity<Unit> quantity1 = new Quantity<>(q1.getValue(), u1);
        Quantity<Unit> quantity2 = new Quantity<>(q2.getValue(), u2);
        return quantity1.divide(quantity2);
    }

    @Override
    public QuantityDTO convert(QuantityDTO quantity, String toUnit) {
        Unit fromUnit = resolveUnit(quantity.getUnit());
        Unit targetUnit = resolveUnit(toUnit);
        validateSameCategory(fromUnit, targetUnit);

        fromUnit.validateOperationSupport("conversion");
        targetUnit.validateOperationSupport("conversion");

        double baseValue = fromUnit.convertToBaseUnit(quantity.getValue());
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);

        return new QuantityDTO(convertedValue, targetUnit.toString());
    }

    private Unit resolveUnit(String unit) {
        if (unit == null || unit.isBlank()) {
            throw new InvalidUnitException("Unit is required");
        }

        String normalized = unit.trim().toUpperCase(Locale.ROOT);

        if (isInEnum(normalized, LengthUnit.values())) {
            return LengthUnit.valueOf(normalized);
        }
        if (isInEnum(normalized, WeightUnit.values())) {
            return WeightUnit.valueOf(normalized);
        }
        if (isInEnum(normalized, VolumeUnit.values())) {
            return VolumeUnit.valueOf(normalized);
        }
        if (isInEnum(normalized, TemperatureUnit.values())) {
            return TemperatureUnit.valueOf(normalized);
        }

        throw new InvalidUnitException("Invalid unit: " + unit);
    }

    private <T extends Enum<T>> boolean isInEnum(String value, T[] values) {
        for (T candidate : values) {
            if (candidate.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

    private void validateSameCategory(Unit u1, Unit u2) {
        if (u1.getCategory() != u2.getCategory()) {
            throw new InvalidUnitException("Different unit categories not allowed");
        }
    }
}
