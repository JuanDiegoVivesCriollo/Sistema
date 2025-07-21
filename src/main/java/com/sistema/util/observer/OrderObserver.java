package com.sistema.util.observer;

// Patrón Observer - Interfaz para observadores de pedidos
public interface OrderObserver {
    void onOrderStatusChanged(int orderId, String newStatus, String customerEmail);
}
