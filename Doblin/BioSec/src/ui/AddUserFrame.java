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

public class AddUserFrame extends JFrame {
    private JTextField txtUsername = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);
    private JButton btnAdd = new JButton("Add User");
    private String currentUser; // logged-in admin

    public AddUserFrame(String currentUser) {
        this.currentUser = currentUser;

        setTitle("Add User");
        setLayout(new java.awt.FlowLayout());
        add(new JLabel("Username:")); add(txtUsername);
        add(new JLabel("Password:")); add(txtPassword);
        add(btnAdd);

        btnAdd.addActionListener(e -> addUser());

        setSize(300, 250);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void addUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (username, password, permission) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, "Read Only"); // default permission
            pst.executeUpdate();

            logAction(conn, "Added user: " + username);
            JOptionPane.showMessageDialog(this, "User added successfully!");
            this.dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage());
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