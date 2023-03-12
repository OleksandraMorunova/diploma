package com.diploma.assistant.model.enumaration;

public enum TypeUserEnum {
    ADMIN("ADMIN"),
    USER("USER");

    private final String typeUserName;

    TypeUserEnum(String typeUserName) {
        this.typeUserName = typeUserName;
    }

    public String getTypeUserName() {
        return typeUserName;
    }
}
