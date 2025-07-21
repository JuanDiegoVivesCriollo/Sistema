package com.sistema.util.factory;

import com.sistema.util.DiscountStrategy;
import com.sistema.util.strategy.FixedDiscountStrategy;
import com.sistema.util.strategy.PercentageDiscountStrategy;

// Patrón Factory - Fábrica para crear estrategias de descuento
public class DiscountStrategyFactory {
    
    public static DiscountStrategy createDiscountStrategy(String type, double value) {
        switch (type.toLowerCase()) {
            case "porcentaje":
            case "percentage":
                return new PercentageDiscountStrategy(value);
            case "fijo":
            case "fixed":
                return new FixedDiscountStrategy(value);
            default:
                throw new IllegalArgumentException("Tipo de descuento no válido: " + type);
        }
    }
}
