package com.sistema.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sistema.model.Product;
import com.sistema.util.DBConnection;

public class ProductDAOImpl implements ProductDAO {

    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    public void save(Product product) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, imagen_url) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getNombre());
            stmt.setString(2, product.getDescripcion());
            stmt.setDouble(3, product.getPrecio());
            stmt.setInt(4, product.getStock());
            stmt.setInt(5, product.getCategoriaId());
            stmt.setString(6, product.getImagenUrl());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving product", e);
        }
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ?, categoria_id = ?, imagen_url = ? WHERE id_producto = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getNombre());
            stmt.setString(2, product.getDescripcion());
            stmt.setDouble(3, product.getPrecio());
            stmt.setInt(4, product.getStock());
            stmt.setInt(5, product.getCategoriaId());
            stmt.setString(6, product.getImagenUrl());
            stmt.setInt(7, product.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    @Override
    public void delete(int productId) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving products", e);
        }
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getInt("categoria_id"),
                    rs.getString("imagen_url")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving products", e);
        }
        return products;
    }

    @Override
    public Product findById(int productId) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getInt("categoria_id"),
                    rs.getString("imagen_url")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving product by id", e);
        }
        return null;
    }
}