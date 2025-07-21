package com.sistema.model;

public class Category {
    private int id;
    private String nombreCategoria;

    public Category() {}

    public Category(int id, String nombreCategoria) {
        this.id = id;
        this.nombreCategoria = nombreCategoria;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    // Métodos adicionales para compatibilidad con BD
    public int getIdCategoria() { return id; }
    public void setIdCategoria(int id) { this.id = id; }
    
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
    
    @Override
    public String toString() {
        return nombreCategoria;
    }
}