package com.app.qma_service.enums;

import com.app.qma_service.model.Unit;
import com.app.qma_service.model.UnitCategory;

public enum WeightUnit implements Unit {
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.45359237);

    private final double factor;

    WeightUnit(double factor) {
        this.factor = factor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factor;
    }

    @Override
    public UnitCategory getCategory() {
        return UnitCategory.WEIGHT;
    }
}
