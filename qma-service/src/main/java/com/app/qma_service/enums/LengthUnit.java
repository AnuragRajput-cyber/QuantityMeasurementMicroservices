package com.app.qma_service.enums;

import com.app.qma_service.model.Unit;
import com.app.qma_service.model.UnitCategory;

public enum LengthUnit implements Unit {
    METER(1.0),
    CENTIMETER(0.01),
    MILLIMETER(0.001),
    KILOMETER(1000.0),
    INCH(0.0254),
    FEET(0.3048),
    YARD(0.9144);

    private final double factor;

    LengthUnit(double factor) {
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
        return UnitCategory.LENGTH;
    }
}
