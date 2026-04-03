package com.app.qma_service.enums;

import com.app.qma_service.model.Unit;
import com.app.qma_service.model.UnitCategory;

public enum TemperatureUnit implements Unit {
    CELSIUS {
        @Override
        public double convertToBaseUnit(double value) {
            return value + 273.15;
        }

        @Override
        public double convertFromBaseUnit(double baseValue) {
            return baseValue - 273.15;
        }
    },
    FAHRENHEIT {
        @Override
        public double convertToBaseUnit(double value) {
            return (value - 32) * 5 / 9 + 273.15;
        }

        @Override
        public double convertFromBaseUnit(double baseValue) {
            return (baseValue - 273.15) * 9 / 5 + 32;
        }
    },
    KELVIN {
        @Override
        public double convertToBaseUnit(double value) {
            return value;
        }

        @Override
        public double convertFromBaseUnit(double baseValue) {
            return baseValue;
        }
    };

    @Override
    public void validateOperationSupport(String operation) {
        if (!"comparison".equalsIgnoreCase(operation) && !"conversion".equalsIgnoreCase(operation)) {
            throw new UnsupportedOperationException("Temperature does not support operation: " + operation);
        }
    }

    @Override
    public UnitCategory getCategory() {
        return UnitCategory.TEMPERATURE;
    }
}
