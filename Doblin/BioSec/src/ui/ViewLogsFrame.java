/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author abc
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewLogsFrame extends JFrame {
    JTable table = new JTable();

    public ViewLogsFrame() {
        setTitle("System Logs");
        setSize(500, 300);
        loadLogs();
        add(new JScrollPane(table));
        setVisible(true);
    }

    private void loadLogs() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM logs ORDER BY log_time DESC";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Action");
            model.addColumn("Username");
            model.addColumn("Time");

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
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewLogsFrame();
    }
}

