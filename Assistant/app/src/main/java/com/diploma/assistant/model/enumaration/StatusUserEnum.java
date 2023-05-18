package com.diploma.assistant.model.enumaration;

public enum StatusUserEnum {
    ACTIVE("ACTIVE"),
    DEACTIVE("DEACTIVE"),
    BLOCKED("BLOCKED");

    private final String status;

    StatusUserEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
