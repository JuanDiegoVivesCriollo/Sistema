package com.sistema.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sistema.model.Order;
import com.sistema.util.DBConnection;

public class OrderDAOImpl implements OrderDAO {

    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    public void save(Order order) {
        String sql = "INSERT INTO pedidos (id_cliente, fecha_pedido, estado, metodo_pago, metodo_envio, total) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getClienteId());
            stmt.setTimestamp(2, new java.sql.Timestamp(order.getFechaPedido().getTime()));
            stmt.setString(3, order.getEstado());
            stmt.setString(4, order.getMetodoPago());
            stmt.setString(5, order.getMetodoEnvio());
            stmt.setDouble(6, order.getTotal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE pedidos SET id_cliente = ?, fecha_pedido = ?, estado = ?, metodo_pago = ?, metodo_envio = ?, total = ? WHERE id_pedido = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getClienteId());
            stmt.setTimestamp(2, new java.sql.Timestamp(order.getFechaPedido().getTime()));
            stmt.setString(3, order.getEstado());
            stmt.setString(4, order.getMetodoPago());
            stmt.setString(5, order.getMetodoEnvio());
            stmt.setDouble(6, order.getTotal());
            stmt.setInt(7, order.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int orderId) {
        String sql = "DELETE FROM pedidos WHERE id_pedido = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(new Order(
                    rs.getInt("id_pedido"),
                    rs.getInt("id_cliente"),
                    rs.getTimestamp("fecha_pedido"),
                    rs.getString("estado"),
                    rs.getString("metodo_pago"),
                    rs.getString("metodo_envio"),
                    rs.getDouble("total")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public Order findById(int orderId) {
        String sql = "SELECT * FROM pedidos WHERE id_pedido = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Order(
                    rs.getInt("id_pedido"),
                    rs.getInt("id_cliente"),
                    rs.getTimestamp("fecha_pedido"),
                    rs.getString("estado"),
                    rs.getString("metodo_pago"),
                    rs.getString("metodo_envio"),
                    rs.getDouble("total")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}