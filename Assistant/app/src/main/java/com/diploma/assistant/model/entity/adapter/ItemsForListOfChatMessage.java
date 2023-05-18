package com.diploma.assistant.model.entity.adapter;

import com.diploma.assistant.model.entity.registration_service.LoadFile;

import java.io.Serializable;

public class ItemsForListOfChatMessage implements Serializable {
    private String message;
    private LoadFile file;
    private String addedData;
    private String userStatusMessage;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoadFile getFile() {
        return file;
    }

    public void setFile(LoadFile file) {
        this.file = file;
    }

    public String getAddedData() {
        return addedData;
    }

    public void setAddedData(String addedData) {
        this.addedData = addedData;
    }

    public String getUserStatusMessage() {
        return userStatusMessage;
    }

    public void setUserStatusMessage(String userStatusMessage) {
        this.userStatusMessage = userStatusMessage;
    }
}
