package com.sistema.model.dao;

import java.util.List;

import com.sistema.model.Order;

public interface OrderDAO {
    void save(Order order);
    void update(Order order);
    void delete(int orderId);
    List<Order> findAll();
    Order findById(int orderId);
    List<Order> findByCustomerId(int customerId);
}