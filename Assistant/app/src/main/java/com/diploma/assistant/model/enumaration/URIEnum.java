package com.diploma.assistant.model.enumaration;

public enum URIEnum {
    //https://assistant-auth-service.azurewebsites.net/
    //https://192.168.0.102:8442/
    BASE_URL("https://assistant-auth-service.azurewebsites.net/");

    private final String url;

    URIEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
