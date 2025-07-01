package com.sistema.controller;

import java.util.List;

import com.sistema.model.Order;
import com.sistema.model.dao.OrderDAO;
import com.sistema.view.OrderView;

public class OrderController {
    private OrderDAO orderDAO;
    private OrderView orderView;

    public OrderController(OrderDAO orderDAO, OrderView orderView) {
        this.orderDAO = orderDAO;
        this.orderView = orderView;
    }

    public void createOrder(Order order) {
        orderDAO.save(order);
        orderView.showMessage("Pedido creado.");
    }

    public void updateOrder(Order order) {
        orderDAO.update(order);
        orderView.showMessage("Pedido actualizado.");
    }

    public void deleteOrder(int id) {
        orderDAO.delete(id);
        orderView.showMessage("Pedido eliminado.");
    }

    public void showAllOrders() {
        List<Order> orders = orderDAO.findAll();
        orderView.displayOrderList((List<Object>)(List<?>)orders);
    }
}