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

public class BiometricFrame extends JFrame {
    public BiometricFrame() {
        setTitle("Biometric Authentication");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton scanButton = new JButton("Scan Fingerprint");
        scanButton.addActionListener(e -> authenticateFingerprint());

        add(scanButton, BorderLayout.CENTER);
    }

    private void authenticateFingerprint() {
        // Placeholder for biometric logic integration
        JOptionPane.showMessageDialog(this, "Fingerprint matched successfully!");
       // new FilesFrame().setVisible(true);
        this.dispose();
    }
}
