package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.resource_service.CommentsDto;
import com.diploma.assistant.model.entity.resource_service.TaskDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommentsController {

    @POST("api/v1/comments/create/{id}")
    Call<TaskDto> postComment(@Path("id") String id, @Body CommentsDto comment); //@Header("Authorization") String token

    @PUT("api/v1/comments/update/{id}/{idComment}")
    Call<TaskDto> updateComment(@Path("id") String id, @Path("idComment") String idComment);
}
