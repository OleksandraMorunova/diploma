package com.diploma.assistant.model.entity.registration_service;

public class LoadFile {
    private String filename;
    private String fileType;
    private String fileSize;
    private String file;

    public LoadFile() {

    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public String getFileSize() {
        return fileSize;
    }
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }

    public LoadFile(String filename, String fileType, String fileSize, String file) {
        this.filename = filename;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.file = file;
    }
}
