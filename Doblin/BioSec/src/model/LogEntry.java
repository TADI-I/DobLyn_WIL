/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author abc
 */
public class LogEntry {
private int logId;
private String username;
private String fileName;
private String action;
private String timestamp;


public LogEntry(int logId, String username, String fileName, String action, String timestamp) {
this.logId = logId;
this.username = username;
this.fileName = fileName;
this.action = action;
this.timestamp = timestamp;
}


public int getLogId() { return logId; }
public String getUsername() { return username; }
public String getFileName() { return fileName; }
public String getAction() { return action; }
public String getTimestamp() { return timestamp; }
}