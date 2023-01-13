package com.diploma.assistant.model;

public class RegistrationUser {
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userPhone;

    public RegistrationUser( String userFirstName, String userLastName, String userEmail, String userPhone) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
    }


    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }
}
