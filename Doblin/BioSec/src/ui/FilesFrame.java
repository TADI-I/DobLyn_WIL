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
import java.awt.*;

public class FilesFrame extends JFrame {
    private JTable filesTable;

    public FilesFrame() {
        setTitle("Secure Files");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        filesTable = new JTable(new Object[][]{
                {"report.pdf", "Admin", "2025-10-01"},
                {"budget.xlsx", "User1", "2025-09-29"}
        }, new Object[]{"File Name", "Owner", "Last Modified"});

        JButton viewBtn = new JButton("View File");
        viewBtn.addActionListener(e -> openFileViewer());

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> logout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(viewBtn);
        bottomPanel.add(logoutBtn);

        add(new JScrollPane(filesTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void openFileViewer() {
        new FileViewerFrame().setVisible(true);
    }

    private void logout() {
        new LoginFrame().setVisible(true);
        this.dispose();
    }
}
