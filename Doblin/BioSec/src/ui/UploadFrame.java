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
import java.io.File;


public class UploadFrame extends JFrame {
private JTextField fileNameField;


public UploadFrame() {
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
} else {
JOptionPane.showMessageDialog(this, "File uploaded successfully! (Placeholder)");
this.dispose();
}
}
}
