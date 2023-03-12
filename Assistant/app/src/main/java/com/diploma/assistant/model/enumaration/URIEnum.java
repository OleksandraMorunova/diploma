package com.diploma.assistant.model.enumaration;

public enum URIEnum {
    BASE_URL("https://192.168.0.102:8442/");

    private final String url;

    URIEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
