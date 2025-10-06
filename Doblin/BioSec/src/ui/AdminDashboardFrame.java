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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboardFrame extends JFrame {

    private JTable userTable;
    private String currentUser; // logged-in admin

    public AdminDashboardFrame(String username) {
        this.currentUser = username;

        setTitle("Admin Dashboard");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize table
        userTable = new JTable();
        loadUsersFromDB(); // load users dynamically from DB

        // Buttons
        JButton addUserBtn = new JButton("Add User");
        JButton deleteUserBtn = new JButton("Delete User");
        JButton viewLogsBtn = new JButton("View Logs");
        JButton backBtn = new JButton("Back");

        addUserBtn.addActionListener(e -> addUser());
        deleteUserBtn.addActionListener(e -> deleteUser());
        viewLogsBtn.addActionListener(e -> viewLogs());
        backBtn.addActionListener(e -> backToFiles());

        // Button panel
        JPanel btnPanel = new JPanel();
        btnPanel.add(addUserBtn);
        btnPanel.add(deleteUserBtn);
        btnPanel.add(viewLogsBtn);
        btnPanel.add(backBtn);

        add(new JScrollPane(userTable), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    // Load users dynamically from DB
    private void loadUsersFromDB() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("User ID");
        model.addColumn("Username");
        model.addColumn("Role");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id, username, role FROM users";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("role")
                });
            }

            userTable.setModel(model);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
        }
    }

    private void addUser() {
        AddUserFrame addUserFrame = new AddUserFrame(currentUser);
        addUserFrame.setVisible(true);
    }

    private void deleteUser() {
        DeleteUserFrame deleteUserFrame = new DeleteUserFrame(currentUser);
        deleteUserFrame.setVisible(true);
    }

    private void viewLogs() {
        ViewLogsFrame viewLogsFrame = new ViewLogsFrame(currentUser);
        viewLogsFrame.setVisible(true);
    }

    private void backToFiles() {
        FilesFrame filesFrame = new FilesFrame(currentUser);
        filesFrame.setVisible(true);
        this.dispose();
    }
}