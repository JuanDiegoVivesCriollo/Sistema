package com.sistema.model;

import java.util.Date;

public class Order {
    private int id;
    private int clienteId;
    private Date fechaPedido;
    private String estado;
    private String metodoPago;
    private String metodoEnvio;
    private double total;

    public Order() {}

    public Order(int id, int clienteId, Date fechaPedido, String estado, String metodoPago, String metodoEnvio, double total) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.metodoPago = metodoPago;
        this.metodoEnvio = metodoEnvio;
        this.total = total;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public Date getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(Date fechaPedido) { this.fechaPedido = fechaPedido; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getMetodoEnvio() { return metodoEnvio; }
    public void setMetodoEnvio(String metodoEnvio) { this.metodoEnvio = metodoEnvio; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}