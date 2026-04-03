package com.app.qma_service.model;

import java.util.Objects;

import com.app.qma_service.exception.InvalidUnitException;

public class Quantity<U extends Unit> {

    private static final double EPSILON = 0.0001;

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        this.value = value;
        this.unit = unit;
    }

    public double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    public Quantity<U> add(Quantity<U> other) {
        unit.validateOperationSupport("addition");
        double resultBase = this.toBaseUnit() + other.toBaseUnit();
        return new Quantity<>(unit.convertFromBaseUnit(resultBase), unit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        unit.validateOperationSupport("subtraction");
        double resultBase = this.toBaseUnit() - other.toBaseUnit();
        return new Quantity<>(unit.convertFromBaseUnit(resultBase), unit);
    }

    public double divide(Quantity<U> other) {
        unit.validateOperationSupport("division");
        double divisor = other.toBaseUnit();
        if (Math.abs(divisor) < EPSILON) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return this.toBaseUnit() / divisor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Quantity<?> other)) {
            return false;
        }
        if (unit.getCategory() != other.unit.getCategory()) {
            throw new InvalidUnitException("Different unit categories not allowed");
        }
        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(toBaseUnit() / EPSILON));
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }
}
