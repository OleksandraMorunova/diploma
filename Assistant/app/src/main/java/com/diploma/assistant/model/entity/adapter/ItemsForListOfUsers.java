package com.diploma.assistant.model.entity.adapter;

import android.graphics.Bitmap;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

public class ItemsForListOfUsers implements Serializable {
    private String id;
    private String name;
    private String status;
    private String count;
    private String icon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ItemsForListOfUsers(ItemsForListOfUsersBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.status = builder.status;
        this.count = builder.count;
        this.icon = builder.icon;
    }

    public static class ItemsForListOfUsersBuilder implements Serializable {
        private String id;
        private String name;
        private String status;
        private String count;
        private String icon;

        public ItemsForListOfUsersBuilder(String id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public ItemsForListOfUsersBuilder status(String status){
            this.status = status;
            return this;
        }

        public ItemsForListOfUsersBuilder count(String count){
            this.count = count;
            return this;
        }

        public ItemsForListOfUsersBuilder icon(String icon){
            this.icon = icon;
            return this;
        }
        public ItemsForListOfUsers build(){
            return new ItemsForListOfUsers(this);
        }
    }
}
