/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author abc
 */
import db.DatabaseConnection;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteUserFrame extends JFrame {
    JTextField txtUsername = new JTextField(20);
    JButton btnDelete = new JButton("Delete User");

    public DeleteUserFrame() {
        setTitle("Delete User");
        setLayout(new java.awt.FlowLayout());
        add(new JLabel("Username:")); add(txtUsername);
        add(btnDelete);

        btnDelete.addActionListener(e -> deleteUser());
        setSize(300, 120);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void deleteUser() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM users WHERE username = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, txtUsername.getText());
            int affected = pst.executeUpdate();

            if (affected > 0) {
                logAction(conn, "Deleted user " + txtUsername.getText());
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "User not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void logAction(Connection conn, String action) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("INSERT INTO logs (action, username) VALUES (?, ?)");
        pst.setString(1, action);
        pst.setString(2, "admin");
        pst.executeUpdate();
    }

    public static void main(String[] args) {
        new DeleteUserFrame();
    }
}

