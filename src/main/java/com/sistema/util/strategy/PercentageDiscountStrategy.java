package com.sistema.util.strategy;

import com.sistema.util.DiscountStrategy;

// Patrón Strategy - Estrategia de descuento por porcentaje
public class PercentageDiscountStrategy implements DiscountStrategy {
    private double percentage;

    public PercentageDiscountStrategy(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double applyDiscount(double amount) {
        return amount * (1 - percentage / 100);
    }
}
