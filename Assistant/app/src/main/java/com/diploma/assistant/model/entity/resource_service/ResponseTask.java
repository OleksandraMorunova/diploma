package com.diploma.assistant.model.entity.resource_service;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResponseTask implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("userId")
    private String userId;

    @SerializedName("taskId")
    private String taskId;

    @SerializedName("text")
    private String text;

    @SerializedName("files")
    private List<String> files;

    @SerializedName("addedData")
    private String addedData;

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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getAddedData() {
        return addedData;
    }

    public void setAddedData(String addedData) {
        this.addedData = addedData;
    }
}