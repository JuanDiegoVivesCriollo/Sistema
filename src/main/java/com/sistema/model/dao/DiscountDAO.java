package com.sistema.model.dao;

import java.util.List;

import com.sistema.model.Discount;

public interface DiscountDAO {
    void save(Discount discount);
    void update(Discount discount);
    void delete(int discountId);
    List<Discount> findAll();
    Discount findById(int discountId);
}