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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Secure File System - Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Notice message
        JTextArea noticeArea = new JTextArea(
            "Regarding the circulation of documents — it has come to the attention of management that several of our documents are being circulated to unauthorized persons.\n\n" +
            "A person found in violation of this rule will have their account permanently disabled and reported to management.\n\n" +
            "Upon this notice, this system has been locked to prevent any further leaks. Only authorized staff have access.\n\n" +
            "Users who wish to regain access must contact the system administrator.\n\n" +
            "Note that this system is government property. Unauthorized use or copying of any document, text, or material from this network without official permission is a punishable offense under government policy."
        );
        noticeArea.setWrapStyleWord(true);
        noticeArea.setLineWrap(true);
        noticeArea.setEditable(false);
        noticeArea.setFocusable(false);
        noticeArea.setBackground(getBackground());
        JScrollPane scrollPane = new JScrollPane(noticeArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Notice"));

        // Login form
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> handleLogin());
        panel.add(loginBtn);

        JButton biometricBtn = new JButton("Use Fingerprint");
        biometricBtn.addActionListener(e -> openBiometricPage());
        panel.add(biometricBtn);

        add(scrollPane, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");

                logAction(conn, "Login successful", username);

                JOptionPane.showMessageDialog(this, "Login successful!");

                if ("admin".equalsIgnoreCase(role)) {
                    new AdminDashboardFrame(username).setVisible(true); // pass username
                } else {
                    new FilesFrame(username).setVisible(true); // pass username
                }
                this.dispose();
            } else {
                logAction(conn, "Login failed", username);
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void logAction(Connection conn, String action, String username) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(
            "INSERT INTO logs (action, username) VALUES (?, ?)"
        );
        pst.setString(1, action);
        pst.setString(2, username);
        pst.executeUpdate();
    }

    private void openBiometricPage() {
        new BiometricFrame().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
    }
}