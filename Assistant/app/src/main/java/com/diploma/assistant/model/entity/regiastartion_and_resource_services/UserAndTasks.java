package com.diploma.assistant.model.entity.regiastartion_and_resource_services;

import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.entity.resource_service.TaskDto;

import java.util.List;

public class UserAndTasks {
    private User userDto;
    private List<TaskDto> taskDto;

    public User getUserDto() {
        return userDto;
    }

    public void setUserDto(User userDto) {
        this.userDto = userDto;
    }

    public List<TaskDto> getTaskDto() {
        return taskDto;
    }

    public void setTaskDto(List<TaskDto> taskDto) {
        this.taskDto = taskDto;
    }
}
