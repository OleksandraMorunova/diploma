package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.resource_service.TaskDto;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface TasksController {
    @GET("api/tasks/list")
    Call<List<TaskDto>> getComments(@Header("Authorization") String token);

    @GET("api/tasks/list/{idUser}")
    Call<List<TaskDto>> getAllTasksByIdUser(@Header("Authorization") String token, @Path("idUser") String idUser);

    @GET("api/tasks/{idTask}")
    Call<TaskDto> getTaskByIDTask(@Header("Authorization") String token, @Path("idTask") String idTask);

    @Multipart
    @POST("api/tasks/create")
    Call<Void> createTask(@Header("Authorization") String token, @Part("json") TaskDto taskDto, @Part List<MultipartBody.Part> file);
    
    @DELETE("api/tasks/delete/{idTask}")
    Call<Void> deleteTask(@Header("Authorization") String token, @Path("idTask") String idTask);

    @Multipart
    @PUT("api/tasks/update/{idTask}")
    Call<TaskDto> updateTask(@Header("Authorization") String token, @Path("idTask") String idTask, @Part("json") TaskDto taskDto, @Part List<MultipartBody.Part> file);
}