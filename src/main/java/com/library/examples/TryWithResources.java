package com.library.examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.library.kernel.classes.Colors;
import com.library.kernel.framework.EnvReader;

/*
 * Ejemplo de uso de try-with-resources con JDBC para conectarse a PostgreSQL
 * y ejecutar una consulta simple.
 */

public class TryWithResources implements Colors {

    // Cambiar estos valores según tu base de datos
    private static final String URL = "jdbc:postgresql://localhost:" + EnvReader.getEnv("DATABASE_PORT") + "/" + EnvReader.getEnv("DATABASE_NAME");
    private static final String USER = EnvReader.getEnv("DATABASE_USER");
    private static final String PASSWORD = EnvReader.getEnv("DATABASE_PASSWORD");

    public static void main(String[] args) {

        System.out.println(BLUE + "Ejemplo de Try-With-Resources con JDBC" + RESET);

        String query = "SELECT 1 AS id, 'juan' AS nombre"; // Ejemplo de consulta

        System.out.println(BLUE + "=== Ejemplo 1: Try-With-Resources ===" + RESET);
        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()
        ) {
            System.out.println(GREEN + "Conexión exitosa y consulta ejecutada" + RESET);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                System.out.println("ID: " + id + ", Nombre: " + nombre);
            }
        } catch (SQLException e) {
            System.out.println(RED + "Error de SQL: " + e.getMessage() + RESET);
        }

        System.out.println(BLUE + "\n=== Ejemplo 2: Sin Try-With-Resources (manual) ===" + RESET);

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            System.out.println(GREEN + "Conexión exitosa y consulta ejecutada (manual)" + RESET);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                System.out.println("ID: " + id + ", Nombre: " + nombre);
            }

        } catch (SQLException e) {
            System.out.println(RED + "Error de SQL: " + e.getMessage() + RESET);
        } finally {
            // Aquí debemos cerrar los recursos manualmente
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                System.out.println(GREEN + "Recursos cerrados manualmente" + RESET);
            } catch (SQLException e) {
                System.out.println(RED + "Error al cerrar recursos: " + e.getMessage() + RESET);
            }
        }

        System.out.println(BLUE + "=== Fin del ejemplo ===" + RESET);
    }
}