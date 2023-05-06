package com.diploma.assistant.model.entity.registration_service;

public class RefreshJwtRequest {
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshJwtRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}