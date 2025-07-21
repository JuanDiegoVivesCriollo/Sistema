package com.sistema.model;

public class Discount {
    private int id;
    private String nombre;
    private String tipoDescuento; // "porcentaje" o "fijo"
    private double valor;
    private String descripcion;
    private String aplicablePara; // "producto", "categoria", "general"
    private int objetoId; // ID del producto o categoría específica (0 para general)
    private boolean activo;

    public Discount() {
        this.aplicablePara = "general";
        this.objetoId = 0;
        this.activo = true;
    }

    public Discount(int id, String nombre, String tipoDescuento, double valor, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.tipoDescuento = tipoDescuento;
        this.valor = valor;
        this.descripcion = descripcion;
        this.aplicablePara = "general";
        this.objetoId = 0;
        this.activo = true;
    }

    // Getters and Setters existentes
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoDescuento() { return tipoDescuento; }
    public void setTipoDescuento(String tipoDescuento) { this.tipoDescuento = tipoDescuento; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    // Nuevos getters y setters
    public String getAplicablePara() { return aplicablePara; }
    public void setAplicablePara(String aplicablePara) { this.aplicablePara = aplicablePara; }
    
    public int getObjetoId() { return objetoId; }
    public void setObjetoId(int objetoId) { this.objetoId = objetoId; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    @Override
    public String toString() {
        return nombre + " (" + valor + (tipoDescuento.equals("porcentaje") ? "%" : "$") + ")";
    }
}