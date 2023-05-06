package com.diploma.assistant.model.enumaration;

public enum URIEnum {
    BASE_URL("https://192.168.0.103:8442/"),
    RESOURCE_URL("http://192.168.0.103:8443/");

    private final String url;

    URIEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
