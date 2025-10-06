/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

/**
 *
 * @author abc
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:derby://localhost:1527/secure_files_db;create=true"; // create DB if missing
    private static final String USER = "root";
    private static final String PASSWORD = "123456789";

    static {
        // This static block runs once when the class is first loaded
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // USERS TABLE
            stmt.executeUpdate(
                "CREATE TABLE users (" +
                "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "role VARCHAR(20) DEFAULT 'user'" +
                ")"
            );
            System.out.println("✅ Table 'users' created successfully.");

        } catch (SQLException e) {
            if (!e.getSQLState().equals("X0Y32")) { // X0Y32 means table already exists
                e.printStackTrace();
            }
        }

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // FILES TABLE - Updated with both file_content and optional filepath
            stmt.executeUpdate(
                "CREATE TABLE files (" +
                "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                "filename VARCHAR(255) NOT NULL, " +
                "file_content BLOB, " +           // Store actual file content
                "filepath VARCHAR(500), " +       // Optional: keep for backward compatibility
                "file_size BIGINT, " +            // File size in bytes
                "file_type VARCHAR(100), " +      // Optional: MIME type or file extension
                "uploaded_by VARCHAR(50), " +
                "upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            System.out.println("✅ Table 'files' created successfully.");

        } catch (SQLException e) {
            if (!e.getSQLState().equals("X0Y32")) {
                e.printStackTrace();
            }
}
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // LOGS TABLE (optional if you use logging)
            stmt.executeUpdate(
                "CREATE TABLE logs (" +
                "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                "action VARCHAR(255), " +
                "username VARCHAR(50), " +
                "log_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            System.out.println("✅ Table 'logs' created successfully.");

        } catch (SQLException e) {
            if (!e.getSQLState().equals("X0Y32")) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}