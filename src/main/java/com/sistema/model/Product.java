package com.sistema.model;

public class Product {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private int categoriaId;
    private String imagenUrl;

    public Product() {}

    public Product(int id, String nombre, String descripcion, double precio, int stock, int categoriaId, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoriaId = categoriaId;
        this.imagenUrl = imagenUrl;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    // Métodos adicionales para compatibilidad con BD
    public int getIdProducto() { return id; }
    public void setIdProducto(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    
    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }
}