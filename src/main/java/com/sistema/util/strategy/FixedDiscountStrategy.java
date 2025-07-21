package com.sistema.util.strategy;

import com.sistema.util.DiscountStrategy;

// Patrón Strategy - Estrategia de descuento fijo
public class FixedDiscountStrategy implements DiscountStrategy {
    private final double fixedAmount;

    public FixedDiscountStrategy(double fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    @Override
    public double applyDiscount(double amount) {
        return Math.max(0, amount - fixedAmount);
    }
}
