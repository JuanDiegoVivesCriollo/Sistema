package com.sistema.model;

public class Customer {
    private int id;
    private int userId;
    private String nombre;
    private String direccion;
    private String telefono;

    public Customer() {}

    public Customer(int id, int userId, String nombre, String direccion, String telefono) {
        this.id = id;
        this.userId = userId;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}