package com.diploma.assistant.model.entity.resource_service;

public class Chat {
    private String id;
    private String userId;
    private String message;
    private String addedData;
    private String userStatusMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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