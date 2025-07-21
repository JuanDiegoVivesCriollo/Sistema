package com.sistema.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sistema.model.Discount;
import com.sistema.util.DBConnection;

public class DiscountDAOImpl implements DiscountDAO {

    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }    @Override
    public boolean save(Discount discount) {
        String sql = "INSERT INTO descuentos (nombre, tipo_descuento, valor, descripcion) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, discount.getNombre());
            stmt.setString(2, discount.getTipoDescuento());
            stmt.setDouble(3, discount.getValor());
            stmt.setString(4, discount.getDescripcion());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving discount: " + e.getMessage());
            return false;
        }
    }    @Override
    public boolean update(Discount discount) {
        String sql = "UPDATE descuentos SET nombre = ?, tipo_descuento = ?, valor = ?, descripcion = ? WHERE id_descuento = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, discount.getNombre());
            stmt.setString(2, discount.getTipoDescuento());
            stmt.setDouble(3, discount.getValor());
            stmt.setString(4, discount.getDescripcion());
            stmt.setInt(5, discount.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating discount: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int discountId) {
        String sql = "DELETE FROM descuentos WHERE id_descuento = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, discountId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting discount: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Discount> findAll() {
        List<Discount> discounts = new ArrayList<>();
        String sql = "SELECT * FROM descuentos";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                discounts.add(new Discount(
                    rs.getInt("id_descuento"),
                    rs.getString("nombre"),
                    rs.getString("tipo_descuento"),
                    rs.getDouble("valor"),
                    rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving discounts: " + e.getMessage());
        }
        return discounts;
    }

    @Override
    public Discount findById(int discountId) {
        String sql = "SELECT * FROM descuentos WHERE id_descuento = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, discountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Discount(
                    rs.getInt("id_descuento"),
                    rs.getString("nombre"),
                    rs.getString("tipo_descuento"),
                    rs.getDouble("valor"),
                    rs.getString("descripcion")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving discount by ID: " + e.getMessage());
        }
        return null;
    }
}