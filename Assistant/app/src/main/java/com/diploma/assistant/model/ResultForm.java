package com.diploma.assistant.model;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public enum ResultForm {
    SUCCESS(TRUE), ERROR(FALSE);
    private boolean status;

    ResultForm(Boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    //TODO: пусті рядки,відмова
    public void setStatus(boolean status) {
        this.status = status;
    }
}
