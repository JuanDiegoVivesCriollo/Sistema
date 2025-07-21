package com.sistema.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sistema.model.OrderDetail;
import com.sistema.util.DBConnection;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Override
    public boolean save(OrderDetail orderDetail) {
        String sql = "INSERT INTO detalles_pedido (id_pedido, id_producto, cantidad, precio) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderDetail.getIdPedido());
            stmt.setInt(2, orderDetail.getIdProducto());
            stmt.setInt(3, orderDetail.getCantidad());
            stmt.setDouble(4, orderDetail.getPrecio());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar detalle de pedido: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(OrderDetail orderDetail) {
        String sql = "UPDATE detalles_pedido SET id_pedido = ?, id_producto = ?, cantidad = ?, precio = ? WHERE id_detalle = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderDetail.getIdPedido());
            stmt.setInt(2, orderDetail.getIdProducto());
            stmt.setInt(3, orderDetail.getCantidad());
            stmt.setDouble(4, orderDetail.getPrecio());
            stmt.setInt(5, orderDetail.getIdDetalle());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar detalle de pedido: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM detalles_pedido WHERE id_detalle = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle de pedido: " + e.getMessage());
            return false;
        }
    }

    @Override
    public OrderDetail findById(int id) {
        String sql = "SELECT * FROM detalles_pedido WHERE id_detalle = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractOrderDetailFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar detalle de pedido: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<OrderDetail> findAll() {
        List<OrderDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM detalles_pedido";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                details.add(extractOrderDetailFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles de pedido: " + e.getMessage());
        }
        return details;
    }

    @Override
    public List<OrderDetail> findByOrderId(int orderId) {
        List<OrderDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM detalles_pedido WHERE id_pedido = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                details.add(extractOrderDetailFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles del pedido: " + e.getMessage());
        }
        return details;
    }

    private OrderDetail extractOrderDetailFromResultSet(ResultSet rs) throws SQLException {
        OrderDetail detail = new OrderDetail();
        detail.setIdDetalle(rs.getInt("id_detalle"));
        detail.setIdPedido(rs.getInt("id_pedido"));
        detail.setIdProducto(rs.getInt("id_producto"));
        detail.setCantidad(rs.getInt("cantidad"));
        detail.setPrecio(rs.getDouble("precio"));
        return detail;
    }
}
