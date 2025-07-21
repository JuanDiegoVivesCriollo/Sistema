package com.sistema.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sistema.model.Category;
import com.sistema.util.DBConnection;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public boolean save(Category category) {
        String sql = "INSERT INTO categorias (nombre_categoria) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getNombreCategoria());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving category: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Category category) {
        String sql = "UPDATE categorias SET nombre_categoria = ? WHERE id_categoria = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getNombreCategoria());
            stmt.setInt(2, category.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int categoryId) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Category findById(int categoryId) {
        String sql = "SELECT * FROM categorias WHERE id_categoria = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCategoryFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding category: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categorias";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categories.add(extractCategoryFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving categories: " + e.getMessage());
        }
        return categories;
    }

    private Category extractCategoryFromResultSet(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id_categoria"));
        category.setNombreCategoria(rs.getString("nombre_categoria"));
        return category;
    }
}
