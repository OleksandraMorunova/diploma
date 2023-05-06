package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.resource_service.TaskDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TasksController {
    @GET("api/tasks/list")
    Call<List<TaskDto>> getComments(@Header("Authorization") String token);
}
