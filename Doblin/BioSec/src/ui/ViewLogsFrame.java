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

public class ViewLogsFrame extends JFrame {
    private JTable table = new JTable();
    private String currentUser;

    public ViewLogsFrame(String currentUser) {
        this.currentUser = currentUser;

        setTitle("System Logs");
        setSize(600, 350);
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadLogs();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadLogs() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Action");
        model.addColumn("Username");
        model.addColumn("Time");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM logs ORDER BY log_time DESC";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("action"),
                    rs.getString("username"),
                    rs.getTimestamp("log_time")
                });
            }

            table.setModel(model);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading logs: " + ex.getMessage());
        }
    }
}

