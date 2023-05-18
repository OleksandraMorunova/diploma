package com.diploma.assistant.model.enumaration;

public enum CheckTask {
    NOT_REVIEWED("NOT_REVIEWED"),
    REVIEWED("REVIEWED"),
    RETURNED("RETURNED");

    private final String checkLine;

    CheckTask(String checkLine) {
        this.checkLine = checkLine;
    }

    public String getCheckLine() {
        return checkLine;
    }
}
