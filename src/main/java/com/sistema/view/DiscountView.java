package com.sistema.view;

import java.util.List;

import com.sistema.model.Discount;

public class DiscountView {
    public void showDiscountResult(double discounted) {
        System.out.println("Total con descuento: $" + discounted);
    }
    public void displayDiscountList(List<Discount> discounts) {
        for (Discount d : discounts) {
            System.out.println(d.getNombre() + " - " + d.getTipoDescuento() + ": " + d.getValor());
        }
    }
}