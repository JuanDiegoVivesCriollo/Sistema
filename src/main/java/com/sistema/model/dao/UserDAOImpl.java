package com.sistema.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sistema.model.User;
import com.sistema.util.DBConnection;

public class UserDAOImpl implements UserDAO {

    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    public boolean save(User user) {
        String sql = "INSERT INTO usuarios (email, password, rol) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Log the exception or handle it appropriately
            System.err.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id_usuario"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("rol")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id_usuario"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("rol")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id_usuario"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("rol")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
        return users;
    }

    public boolean registerUserAndCustomer(User user, String nombre, String direccion, String telefono) {
        String userSql = "INSERT INTO usuarios (email, password, rol) VALUES (?, ?, ?)";
        String customerSql = "INSERT INTO clientes (id_usuario, nombre, direccion, telefono) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // 1. Insertar usuario
            try (PreparedStatement userStmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, user.getEmail());
                userStmt.setString(2, user.getPassword());
                userStmt.setString(3, user.getRole());
                int affectedRows = userStmt.executeUpdate();
                if (affectedRows == 0) throw new SQLException("No se pudo crear el usuario.");

                ResultSet generatedKeys = userStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idUsuario = generatedKeys.getInt(1);

                    // Si es cliente, insertar en clientes
                    if ("cliente".equalsIgnoreCase(user.getRole())) {
                        try (PreparedStatement custStmt = conn.prepareStatement(customerSql)) {
                            custStmt.setInt(1, idUsuario);
                            custStmt.setString(2, nombre);
                            custStmt.setString(3, direccion);
                            custStmt.setString(4, telefono);
                            custStmt.executeUpdate();
                        }
                    }
                } else {
                    throw new SQLException("No se obtuvo el ID del usuario.");
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE usuarios SET email = ?, password = ?, rol = ? WHERE id_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setInt(4, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
}