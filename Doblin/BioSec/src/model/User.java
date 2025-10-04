/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author abc
 */
public class User {
private int id;
private String username;
private String passwordHash;
private String role;


public User(int id, String username, String passwordHash, String role) {
this.id = id;
this.username = username;
this.passwordHash = passwordHash;
this.role = role;
}


public int getId() { return id; }
public String getUsername() { return username; }
public String getPasswordHash() { return passwordHash; }
public String getRole() { return role; }
}