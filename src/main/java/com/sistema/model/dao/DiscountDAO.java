package com.sistema.model.dao;

import java.util.List;

import com.sistema.model.Discount;

public interface DiscountDAO {
    boolean save(Discount discount);
    boolean update(Discount discount);
    boolean delete(int discountId);
    List<Discount> findAll();
    Discount findById(int discountId);
}