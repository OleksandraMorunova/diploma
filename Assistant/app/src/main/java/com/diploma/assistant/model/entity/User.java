package com.diploma.assistant.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("registration_data")
    private String registrationData;

    @SerializedName("roles")
    private List<String> roles = new ArrayList<>();

    @SerializedName("status")
    private String status;

    @SerializedName("code")
    private String code;

    @SerializedName("one_time_code_data")
    private String codeData;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRegistrationData() {
        return registrationData;
    }
    public void setRegistrationData(String registrationData) {
        this.registrationData = registrationData;
    }
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getCodeData() {
        return codeData;
    }
    public void setCodeData(String codeData) {
        this.codeData = codeData;
    }
}
