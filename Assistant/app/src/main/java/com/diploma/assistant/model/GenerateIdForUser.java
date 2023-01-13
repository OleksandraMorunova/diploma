package com.diploma.assistant.model;

import java.util.ArrayList;

public class GenerateIdForUser {
    private String userId;

    private ArrayList<RegistrationUser> registrationUsers;

    public GenerateIdForUser(String userId, ArrayList<RegistrationUser> registrationUsers) {
        this.userId = userId;
        this.registrationUsers = registrationUsers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserGenerateID{" +
                "userId='" + userId + '\'' +
                ", registrationUsers=" + registrationUsers +
                '}';
    }
}
