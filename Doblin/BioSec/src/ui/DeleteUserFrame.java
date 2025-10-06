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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteUserFrame extends JFrame {
    private JTextField txtUsername = new JTextField(20);
    private JButton btnDelete = new JButton("Delete User");
    private String currentUser; // logged-in admin

    public DeleteUserFrame(String currentUser) {
        this.currentUser = currentUser;

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
        String username = txtUsername.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM users WHERE username = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            int affected = pst.executeUpdate();

            if (affected > 0) {
                logAction(conn, "Deleted user: " + username);
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "User not found.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage());
        }
    }

    private void logAction(Connection conn, String action) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(
            "INSERT INTO logs (action, username) VALUES (?, ?)"
        );
        pst.setString(1, action);
        pst.setString(2, currentUser);
        pst.executeUpdate();
    }
}