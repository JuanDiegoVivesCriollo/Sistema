package com.sistema.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sistema.model.Notification;
import com.sistema.util.DBConnection;

public class NotificationDAOImpl implements NotificationDAO {

    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    public void save(Notification notification) {
        String sql = "INSERT INTO notificaciones (id_pedido, mensaje, fecha_notificacion) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notification.getPedidoId());
            stmt.setString(2, notification.getMensaje());
            stmt.setTimestamp(3, new java.sql.Timestamp(notification.getFechaNotificacion().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Notification notification) {
        String sql = "UPDATE notificaciones SET id_pedido = ?, mensaje = ?, fecha_notificacion = ? WHERE id_notificacion = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notification.getPedidoId());
            stmt.setString(2, notification.getMensaje());
            stmt.setTimestamp(3, new java.sql.Timestamp(notification.getFechaNotificacion().getTime()));
            stmt.setInt(4, notification.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int notificationId) {
        String sql = "DELETE FROM notificaciones WHERE id_notificacion = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Notification> findAll() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notificaciones";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notifications.add(new Notification(
                    rs.getInt("id_notificacion"),
                    rs.getInt("id_pedido"),
                    rs.getString("mensaje"),
                    rs.getTimestamp("fecha_notificacion")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return notifications;
    }

    @Override
    public Notification findById(int notificationId) {
        String sql = "SELECT * FROM notificaciones WHERE id_notificacion = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Notification(
                    rs.getInt("id_notificacion"),
                    rs.getInt("id_pedido"),
                    rs.getString("mensaje"),
                    rs.getTimestamp("fecha_notificacion")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}