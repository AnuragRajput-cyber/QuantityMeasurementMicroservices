package com.app.qma_service.model;

public interface Unit {

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    default void validateOperationSupport(String operation) {
    }

    UnitCategory getCategory();
}
