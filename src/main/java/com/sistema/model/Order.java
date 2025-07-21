package com.sistema.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sistema.util.observer.OrderObserver;

public class Order {
    private int id;
    private int clienteId;
    private Date fechaPedido;
    private String estado;
    private String metodoPago;
    private String metodoEnvio;
    private double total;
    private List<OrderObserver> observers; // Patrón Observer

    public Order() {
        this.observers = new ArrayList<>();
    }

    public Order(int id, int clienteId, Date fechaPedido, String estado, String metodoPago, String metodoEnvio, double total) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.metodoPago = metodoPago;
        this.metodoEnvio = metodoEnvio;
        this.total = total;
        this.observers = new ArrayList<>();
    }

    // Métodos del patrón Observer
    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String customerEmail) {
        for (OrderObserver observer : observers) {
            observer.onOrderStatusChanged(this.id, this.estado, customerEmail);
        }
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    // Métodos adicionales para compatibilidad
    public int getIdPedido() { return id; }
    public void setIdPedido(int id) { this.id = id; }
    
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public Date getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(Date fechaPedido) { this.fechaPedido = fechaPedido; }
    public String getEstado() { return estado; }
    
    public void setEstado(String estado) {
        this.estado = estado;
        // No notificamos aquí porque necesitamos el email del cliente
    }
    
    public void setEstado(String estado, String customerEmail) {
        this.estado = estado;
        notifyObservers(customerEmail);
    }
    
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getMetodoEnvio() { return metodoEnvio; }
    public void setMetodoEnvio(String metodoEnvio) { this.metodoEnvio = metodoEnvio; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}