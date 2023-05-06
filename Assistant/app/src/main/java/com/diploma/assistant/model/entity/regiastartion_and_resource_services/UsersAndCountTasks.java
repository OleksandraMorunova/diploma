package com.diploma.assistant.model.entity.regiastartion_and_resource_services;


import com.diploma.assistant.model.entity.registration_service.User;

import java.util.List;

public class UsersAndCountTasks {
    private List<User> userList;
    private List<Integer> integerList;

    public UsersAndCountTasks(List<User> userList, List<Integer> integerList) {
        this.userList = userList;
        this.integerList = integerList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Integer> getIntegerList() {
        return integerList;
    }

    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }
}
