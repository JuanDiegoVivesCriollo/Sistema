package com.sistema.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseChecker {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Conexión exitosa a la base de datos!");
            
            // Verificar qué tablas existen
            DatabaseMetaData metadata = conn.getMetaData();
            ResultSet tables = metadata.getTables(null, null, "%", new String[]{"TABLE"});
            
            System.out.println("\nTablas existentes:");
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("- " + tableName);
            }
            
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
