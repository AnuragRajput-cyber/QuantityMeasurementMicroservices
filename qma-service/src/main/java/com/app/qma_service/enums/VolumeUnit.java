package com.app.qma_service.enums;

import com.app.qma_service.model.Unit;
import com.app.qma_service.model.UnitCategory;

public enum VolumeUnit implements Unit {
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double factor;

    VolumeUnit(double factor) {
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
        return UnitCategory.VOLUME;
    }
}
