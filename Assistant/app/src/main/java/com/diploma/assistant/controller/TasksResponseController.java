package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.resource_service.ResponseTask;
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

public interface TasksResponseController {
    @Multipart
    @POST("api/response/create")
    Call<Void> createTaskResponse(@Header("Authorization") String token, @Part("json") ResponseTask task, @Part List<MultipartBody.Part> file);

    @GET("api/response/{idTask}")
    Call<ResponseTask> getAllTaskResponseByIdUser(@Header("Authorization") String token, @Path("idTask") String idTask);

    @DELETE("api/response/delete/{idTask}")
    Call<Void> deleteTaskResponse(@Header("Authorization") String token, @Path("idTask") String idTask);
}