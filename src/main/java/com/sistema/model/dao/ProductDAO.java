package com.sistema.model.dao;

import java.util.List;

import com.sistema.model.Product;

public interface ProductDAO {
    boolean save(Product product);
    boolean update(Product product);
    boolean delete(int productId);
    List<Product> findAll();
    Product findById(int productId);
}