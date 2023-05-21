package com.diploma.assistant.model.enumaration;

public enum URIEnum {
    BASE_URL("https://assistant-auth-service.azurewebsites.net/");

    private final String url;

    URIEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
