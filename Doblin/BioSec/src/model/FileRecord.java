/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author abc
 */
public class FileRecord {
private int fileId;
private String fileName;
private String owner;
private String lastModified;


public FileRecord(int fileId, String fileName, String owner, String lastModified) {
this.fileId = fileId;
this.fileName = fileName;
this.owner = owner;
this.lastModified = lastModified;
}


public int getFileId() { return fileId; }
public String getFileName() { return fileName; }
public String getOwner() { return owner; }
public String getLastModified() { return lastModified; }
}