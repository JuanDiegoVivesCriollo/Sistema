package com.sistema.model;

public class OrderDetail {
    private int id;
    private int pedidoId;
    private int productoId;
    private int cantidad;
    private double precio;

    public OrderDetail() {}

    public OrderDetail(int id, int pedidoId, int productoId, int cantidad, double precio) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    // Métodos adicionales para compatibilidad con BD
    public int getIdDetalle() { return id; }
    public void setIdDetalle(int id) { this.id = id; }
    
    public int getPedidoId() { return pedidoId; }
    public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }
    
    // Métodos adicionales para compatibilidad con BD
    public int getIdPedido() { return pedidoId; }
    public void setIdPedido(int pedidoId) { this.pedidoId = pedidoId; }
    
    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }
    
    // Métodos adicionales para compatibilidad con BD
    public int getIdProducto() { return productoId; }
    public void setIdProducto(int productoId) { this.productoId = productoId; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}