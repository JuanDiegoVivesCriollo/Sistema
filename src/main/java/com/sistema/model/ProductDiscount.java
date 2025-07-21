package com.sistema.model;

public class ProductDiscount {
    private int idProductDiscount;
    private int idProducto;
    private int idDescuento;
    private String fechaInicio;
    private String fechaFin;
    private boolean activo;

    public ProductDiscount() {}

    public ProductDiscount(int idProductDiscount, int idProducto, int idDescuento, String fechaInicio, String fechaFin, boolean activo) {
        this.idProductDiscount = idProductDiscount;
        this.idProducto = idProducto;
        this.idDescuento = idDescuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
    }

    // Getters and Setters
    public int getIdProductDiscount() { return idProductDiscount; }
    public void setIdProductDiscount(int idProductDiscount) { this.idProductDiscount = idProductDiscount; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getIdDescuento() { return idDescuento; }
    public void setIdDescuento(int idDescuento) { this.idDescuento = idDescuento; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
