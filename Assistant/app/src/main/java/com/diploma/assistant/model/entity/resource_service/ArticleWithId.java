package com.diploma.assistant.model.entity.resource_service;

public class ArticleWithId{
    private String taskId;
    private String title;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArticleWithId(String taskId, String title) {
        this.taskId = taskId;
        this.title = title;
    }
}
