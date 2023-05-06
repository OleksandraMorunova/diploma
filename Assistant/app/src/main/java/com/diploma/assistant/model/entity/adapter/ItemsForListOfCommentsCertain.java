package com.diploma.assistant.model.entity.adapter;

public class ItemsForListOfCommentsCertain {
    private String comment_added_data;
    private String comment;

    public String getComment_added_data() {
        return comment_added_data;
    }

    public void setComment_added_data(String comment_added_data) {
        this.comment_added_data = comment_added_data;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ItemsForListOfCommentsCertain(String comment_added_data, String comment) {
        this.comment_added_data = comment_added_data;
        this.comment = comment;
    }
}
