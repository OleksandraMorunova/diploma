package com.diploma.assistant.model.entity.resource_service;

import com.google.gson.annotations.SerializedName;

import org.bson.types.ObjectId;

import java.io.Serializable;

public class CommentsDto implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("user_comment_id")
    private String user_comment_id;

    @SerializedName("comment")
    private String comment;

    @SerializedName("comment_added_data")
    private String comment_added_data;

    @SerializedName("reviewed")
    private Boolean reviewed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_comment_id() {
        return user_comment_id;
    }

    public void setUser_comment_id(String user_comment_id) {
        this.user_comment_id = user_comment_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_added_data() {
        return comment_added_data;
    }

    public void setComment_added_data(String comment_added_data) {
        this.comment_added_data = comment_added_data;
    }

    public Boolean getReviewed() {
        return reviewed;
    }

    public void setReviewed(Boolean reviewed) {
        this.reviewed = reviewed;
    }


}
