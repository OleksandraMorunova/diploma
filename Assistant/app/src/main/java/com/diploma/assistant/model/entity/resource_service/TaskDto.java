package com.diploma.assistant.model.entity.resource_service;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class TaskDto implements Serializable {
    private String id;

    @SerializedName("userId")
    private String userId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("files")
    private List<String> files;

    @SerializedName("addedData")
    private String addedData;

    @SerializedName("updateData")
    private String updateData;

    @SerializedName("comments")
    private List<CommentsDto> comments;

    @SerializedName("audit")
    private String audit;

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
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
    public String getUpdateData() {
        return updateData;
    }
    public void setUpdateData(String updateData) {
        this.updateData = updateData;
    }
    public List<CommentsDto> getComments() {
        return comments;
    }
    public void setComments(List<CommentsDto> comments) {
        this.comments = comments;
    }
    public String getAudit() {
        return audit;
    }
    public void setAudit(String audit) {
        this.audit = audit;
    }
}
