package com.sistema.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sistema.model.Customer;
import com.sistema.util.DBConnection;

public class CustomerDAOImpl implements CustomerDAO {

    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }    @Override
    public boolean save(Customer customer) {
        String sql = "INSERT INTO clientes (id_usuario, nombre, direccion, telefono) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customer.getUserId());
            stmt.setString(2, customer.getNombre());
            stmt.setString(3, customer.getDireccion());
            stmt.setString(4, customer.getTelefono());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar cliente: " + e.getMessage());
            return false;
        }
    }    @Override
    public boolean update(Customer customer) {
        String sql = "UPDATE clientes SET nombre = ?, direccion = ?, telefono = ? WHERE id_cliente = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getNombre());
            stmt.setString(2, customer.getDireccion());
            stmt.setString(3, customer.getTelefono());
            stmt.setInt(4, customer.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int customerId) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getInt("id_cliente"),
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    @Override
    public Customer findById(int customerId) {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                    rs.getInt("id_cliente"),
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}