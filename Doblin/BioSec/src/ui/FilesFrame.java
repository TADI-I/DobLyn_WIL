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



public class FilesFrame extends JFrame {
    private JTable filesTable;
    private String currentUser;
    
    public FilesFrame(String username) {
        this.currentUser = username;
        setTitle("Secure Files");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

         filesTable = new JTable();
        loadFilesFromDB();
     

        JButton viewBtn = new JButton("View File");
        viewBtn.addActionListener(e -> openFileViewer());

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> logout());

        JButton uploadbtn = new JButton("Upload");
        uploadbtn.addActionListener(e -> upload());

               JPanel bottomPanel = new JPanel();
        bottomPanel.add(uploadbtn);
        bottomPanel.add(viewBtn);
        bottomPanel.add(logoutBtn);

        add(new JScrollPane(filesTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
     private void loadFilesFromDB() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT filename, uploaded_by, upload_time FROM files ORDER BY upload_time DESC";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("File Name");
            model.addColumn("Owner");
            model.addColumn("Uploaded On");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("filename"),
                        rs.getString("uploaded_by"),
                        rs.getTimestamp("upload_time")
                });
            }

            filesTable.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading files: " + e.getMessage());
        }
    }

   

private void openFileViewer() {
    int selectedRow = filesTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a file to view.");
        return;
    }

    // Assuming column 2 stores the encrypted file path in the DB table model
    String encryptedFilePath = filesTable.getValueAt(selectedRow, 2).toString();
    new FileViewerFrame(encryptedFilePath).setVisible(true);
}
    
   private void upload() {
    new UploadFrame(currentUser).setVisible(true);
}

    private void logout() {
        new LoginFrame().setVisible(true);
        this.dispose();
    }
}
