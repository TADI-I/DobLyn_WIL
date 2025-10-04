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

public class FileViewerFrame extends JFrame {
    public FileViewerFrame() {
        setTitle("File Viewer (Read-Only)");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea fileContent = new JTextArea("[Decrypted file content displayed here]");
        fileContent.setEditable(false);

        add(new JScrollPane(fileContent), BorderLayout.CENTER);
    }
}
