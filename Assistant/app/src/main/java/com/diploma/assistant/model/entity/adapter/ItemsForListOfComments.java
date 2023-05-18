package com.diploma.assistant.model.entity.adapter;

import java.io.Serializable;

public class ItemsForListOfComments implements Serializable {
    private String idTask;
    private String idCommentByExistList;
    private String userName;
    private final String comment;
    private final String comment_added_data;
    private final Boolean reviewed;
    private final String articleId;
    private final String article;

    public String getIdTask() {
        return idTask;
    }
    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }
    public String getIdCommentsByExistList() {
        return idCommentByExistList;
    }
    public void setIdCommentsByExistList(String idCommentsByExistList) {
        this.idCommentByExistList = idCommentsByExistList;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getComment() {
        return comment;
    }
    public String getComment_added_data() {
        return comment_added_data;
    }
    public Boolean getReviewed() {
        return reviewed;
    }
    public String getArticleId() {
        return articleId;
    }
    public String getArticle() {
        return article;
    }

    public ItemsForListOfComments(ItemsBuilder builder) {
        this.idTask = builder.idTask;
        this.idCommentByExistList = builder.idCommentByExistList;
        this.userName = builder.userName;
        this.comment = builder.comment;
        this.comment_added_data = builder.comment_added_data;
        this.reviewed = builder.reviewed;
        this.articleId = builder.articleId;
        this.article = builder.article;
    }

    public static class ItemsBuilder implements Serializable{
        private String idTask;
        private String idCommentByExistList;
        private final String userName;
        private final String comment;
        private String comment_added_data;
        private Boolean reviewed;
        private String articleId;
        private String article;

        public ItemsBuilder (String userName, String comment) {
            this.userName = userName;
            this.comment = comment;
        }

        public ItemsBuilder idTask(String idTask) {
            this.idTask = idTask;
            return this;
        }

        public ItemsBuilder idComment(String idCommentByExistList) {
            this.idCommentByExistList = idCommentByExistList;
            return this;
        }

        public ItemsBuilder commentAddedData(String comment_added_data) {
            this.comment_added_data = comment_added_data;
            return this;
        }

        public ItemsBuilder reviewed(Boolean reviewed) {
            this.reviewed = reviewed;
            return this;
        }

        public ItemsBuilder articleId(String articleId) {
            this.articleId = articleId;
            return this;
        }

        public ItemsBuilder article(String article) {
            this.article = article;
            return this;
        }

        public ItemsForListOfComments build(){
            return new ItemsForListOfComments(this);
        }
    }
}
