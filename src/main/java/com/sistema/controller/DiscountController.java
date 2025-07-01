package com.sistema.controller;

import java.util.List;

import com.sistema.model.Discount;
import com.sistema.model.dao.DiscountDAO;
import com.sistema.util.DiscountStrategy;
import com.sistema.view.DiscountView;

public class DiscountController {
    private DiscountDAO discountDAO;
    private DiscountView discountView;
    private DiscountStrategy discountStrategy;

    public DiscountController(DiscountDAO discountDAO, DiscountView discountView, DiscountStrategy discountStrategy) {
        this.discountDAO = discountDAO;
        this.discountView = discountView;
        this.discountStrategy = discountStrategy;
    }

    public void applyDiscount(int orderId, double amount) {
        double discounted = discountStrategy.applyDiscount(amount);
        discountView.showDiscountResult(discounted);
    }

    public void showAllDiscounts() {
        List<Discount> discounts = discountDAO.findAll();
        discountView.displayDiscountList(discounts);
    }
}