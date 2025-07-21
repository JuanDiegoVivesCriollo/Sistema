package com.sistema.model.dao;

import java.util.List;

import com.sistema.model.OrderDetail;

public interface OrderDetailDAO {
    boolean save(OrderDetail orderDetail);
    boolean update(OrderDetail orderDetail);
    boolean delete(int id);
    OrderDetail findById(int id);
    List<OrderDetail> findAll();
    List<OrderDetail> findByOrderId(int orderId);
}
