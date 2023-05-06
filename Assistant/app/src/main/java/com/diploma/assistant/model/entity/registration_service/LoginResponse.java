package com.diploma.assistant.model.entity.registration_service;

public class LoginResponse {
    private String accessToken;
    private String refreshToken;

    public String getType() {
        return "Bearer ";
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
