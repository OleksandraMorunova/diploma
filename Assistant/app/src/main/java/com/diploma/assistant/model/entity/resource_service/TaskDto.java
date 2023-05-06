package com.diploma.assistant.model.entity.resource_service;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;


public class TaskDto implements Serializable {
    private String id;
    private ObjectId userId;
    private String title;
    private String description;
    private List<ObjectId> documents;
    private String addedData;
    private String updateData;
    private List<CommentsDto> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
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

    public List<ObjectId> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ObjectId> documents) {
        this.documents = documents;
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
}
