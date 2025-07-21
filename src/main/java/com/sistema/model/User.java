package com.sistema.model;

public class User {
    private int id;
    private String email;
    private String password;
    private String role; // "cliente" o "admin"

    public User() {}

    public User(int id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    // Métodos adicionales para compatibilidad
    public int getIdUsuario() { return id; }
    public void setIdUsuario(int id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    // Métodos adicionales para compatibilidad
    public String getRol() { return role; }
    public void setRol(String role) { this.role = role; }
}