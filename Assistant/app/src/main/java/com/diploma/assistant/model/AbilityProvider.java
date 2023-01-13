package com.diploma.assistant.model;

public enum AbilityProvider {
    ADMIN("1"), USER("0");

    private String userPermission;

    AbilityProvider(String userPermission) {
        this.userPermission = userPermission;
    }

    public String getUserPermission() {
        return userPermission;
    }
}
