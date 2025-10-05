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

public class AddUserFrame extends JFrame {
    JTextField txtUsername = new JTextField(20);
    JPasswordField txtPassword = new JPasswordField(20);
    JButton btnAdd = new JButton("Add User");

    public AddUserFrame() {
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
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, txtUsername.getText());
            pst.setString(2, new String(txtPassword.getPassword()));
            pst.executeUpdate();

            logAction(conn, "Added user " + txtUsername.getText());
            JOptionPane.showMessageDialog(this, "User added successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void logAction(Connection conn, String action) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("INSERT INTO logs (action, username) VALUES (?, ?)");
        pst.setString(1, action);
        pst.setString(2, "admin"); // or current logged-in user
        pst.executeUpdate();
    }

    public static void main(String[] args) {
        new AddUserFrame();
    }
}

