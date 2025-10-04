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


public class AdminDashboardFrame extends JFrame {
private JTable userTable;


public AdminDashboardFrame() {
setTitle("Admin Dashboard");
setSize(700, 450);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLocationRelativeTo(null);


userTable = new JTable(new Object[][]{
{"1", "admin", "Full Access"},
{"2", "user1", "Read Only"}
}, new Object[]{"User ID", "Username", "Permission"});


JButton addUserBtn = new JButton("Add User");
JButton deleteUserBtn = new JButton("Delete User");
JButton viewLogsBtn = new JButton("View Logs");
JButton backBtn = new JButton("Back");


addUserBtn.addActionListener(e -> addUser());
deleteUserBtn.addActionListener(e -> deleteUser());
viewLogsBtn.addActionListener(e -> viewLogs());
backBtn.addActionListener(e -> backToFiles());


JPanel btnPanel = new JPanel();
btnPanel.add(addUserBtn);
btnPanel.add(deleteUserBtn);
btnPanel.add(viewLogsBtn);
btnPanel.add(backBtn);


add(new JScrollPane(userTable), BorderLayout.CENTER);
add(btnPanel, BorderLayout.SOUTH);
}


private void addUser() {
 new AddUserFrame();
        dispose(); 
}


private void deleteUser() {
new DeleteUserFrame();
        dispose();}


private void viewLogs() {
 new ViewLogsFrame();
        dispose();}


private void backToFiles() {
new FilesFrame().setVisible(true);
this.dispose();
}
}
