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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class FileViewerFrame extends JFrame {

    private static final String AES_KEY = "1234567890123456"; // must match UploadFrame

    public FileViewerFrame(int fileId) {
        setTitle("File Viewer (Read-Only)");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea fileContent = new JTextArea();
        fileContent.setEditable(false);

        try {
            String decryptedText = decryptFileFromDatabase(fileId);
            if (decryptedText != null) {
                fileContent.setText(decryptedText);
            } else {
                fileContent.setText("⚠️ File not found in database.");
            }
        } catch (Exception ex) {
            fileContent.setText("❌ Error decrypting file: " + ex.getMessage());
            ex.printStackTrace();
        }

        add(new JScrollPane(fileContent), BorderLayout.CENTER);
    }

    // Alternative constructor that takes filename for convenience
    public FileViewerFrame(String filename) {
        this(getFileIdFromFilename(filename));
    }

    private String decryptFileFromDatabase(int fileId) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT filename, file_content FROM files WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, fileId);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String filename = rs.getString("filename");
                byte[] encryptedData = rs.getBytes("file_content");
                
                if (encryptedData != null) {
                    // Decrypt the content
                    Cipher cipher = Cipher.getInstance("AES");
                    SecretKey secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
                    cipher.init(Cipher.DECRYPT_MODE, secretKey);
                    
                    byte[] decryptedBytes = cipher.doFinal(encryptedData);
                    
                    // Check if file is text-based by extension
                    if (isTextFile(filename)) {
                        return new String(decryptedBytes);
                    } else {
                        return "📁 Binary file: " + filename + 
                               "\nSize: " + decryptedBytes.length + " bytes" +
                               "\n\nThis appears to be a binary file. Cannot display as text.";
                    }
                }
            }
        }
        return null;
    }

    private static int getFileIdFromFilename(String filename) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id FROM files WHERE filename = ? ORDER BY upload_time DESC LIMIT 1";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, filename);
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1; // Return -1 if not found
    }

    private boolean isTextFile(String filename) {
        String[] textExtensions = {".txt", ".java", ".xml", ".html", ".css", ".js", ".json", ".csv", ".log", ".md"};
        String lowerFilename = filename.toLowerCase();
        
        for (String ext : textExtensions) {
            if (lowerFilename.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    // Method to get file info for display
    public static String getFileInfo(int fileId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT filename, file_size, uploaded_by, upload_time FROM files WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, fileId);
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return String.format(
                    "File: %s\nSize: %d bytes\nUploaded by: %s\nDate: %s",
                    rs.getString("filename"),
                    rs.getLong("file_size"),
                    rs.getString("uploaded_by"),
                    rs.getTimestamp("upload_time")
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "File information not available";
    }

    // Method to download/save decrypted file
    public static void saveDecryptedFile(int fileId, String outputPath) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT filename, file_content FROM files WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, fileId);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String filename = rs.getString("filename");
                byte[] encryptedData = rs.getBytes("file_content");
                
                if (encryptedData != null) {
                    // Decrypt the content
                    Cipher cipher = Cipher.getInstance("AES");
                    SecretKey secretKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");
                    cipher.init(Cipher.DECRYPT_MODE, secretKey);
                    
                    byte[] decryptedBytes = cipher.doFinal(encryptedData);
                    
                    // Save to file
                    File outputFile = new File(outputPath, filename);
                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        fos.write(decryptedBytes);
                    }
                    
                    JOptionPane.showMessageDialog(null, 
                        "✅ File saved to: " + outputFile.getAbsolutePath());
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "❌ Error saving file: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}