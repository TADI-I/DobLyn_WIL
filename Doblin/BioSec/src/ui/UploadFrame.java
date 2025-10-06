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
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UploadFrame extends JFrame {
    private JTextField fileNameField;
    private static final String AES_KEY = "1234567890123456"; // 16 chars = 128-bit AES key

    private String currentUser;

    public UploadFrame(String username) {
        this.currentUser = username;

        setTitle("Upload File");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        fileNameField = new JTextField();
        JButton browseBtn = new JButton("Browse");
        JButton uploadBtn = new JButton("Upload");

        browseBtn.addActionListener(e -> browseFile());
        uploadBtn.addActionListener(e -> uploadFile());

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("File:"));
        panel.add(fileNameField);
        panel.add(browseBtn);
        panel.add(uploadBtn);

        add(panel);
    }

    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            fileNameField.setText(file.getAbsolutePath());
        }
    }

    private void uploadFile() {
        String filePath = fileNameField.getText();

        if (filePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a file first.");
            return;
        }

        File originalFile = new File(filePath);
        String fileName = originalFile.getName();

        try {
            // Read and encrypt the file content
            byte[] encryptedFileContent = encryptFileContent(originalFile);

            // Save file info and content in DB
            try (Connection conn = DatabaseConnection.getConnection()) {
                // First, update your database table to include a column for file content
                // ALTER TABLE files ADD COLUMN file_content LONGBLOB;
                String query = "INSERT INTO files (filename, file_content, uploaded_by, upload_time) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, fileName);
                pst.setBytes(2, encryptedFileContent); // Store encrypted content as BLOB
                pst.setString(3, currentUser);

                int rows = pst.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "✅ File uploaded, encrypted and stored in database successfully!");

                    // Optional: add log entry
                    try (PreparedStatement logStmt = conn.prepareStatement(
                            "INSERT INTO logs (action, username) VALUES (?, ?)"
                    )) {
                        logStmt.setString(1, "Uploaded & encrypted file: " + fileName);
                        logStmt.setString(2, currentUser);
                        logStmt.executeUpdate();
                    }

                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ Upload failed. Please try again.");
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private byte[] encryptFileContent(File inputFile) throws Exception {
        // Read the file content
        byte[] inputBytes = Files.readAllBytes(inputFile.toPath());

        // Encrypt the content
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(inputBytes);
    }
}