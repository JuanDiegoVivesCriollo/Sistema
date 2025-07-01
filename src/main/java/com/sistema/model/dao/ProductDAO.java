package com.sistema.model.dao;

import java.util.List;

import com.sistema.model.Product;

public interface ProductDAO {
    void save(Product product);
    void update(Product product);
    void delete(int productId);
    List<Product> findAll();
    Product findById(int productId);
}