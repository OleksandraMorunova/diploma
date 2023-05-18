package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.resource_service.CommentsDto;
import com.diploma.assistant.model.entity.resource_service.TaskDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommentsController {
    @POST("api/comments/create/{id}")
    Call<TaskDto> postComment(@Header("Authorization") String token, @Path("id") String id, @Body CommentsDto comment);

    @PUT("api/comments/update/{id}/{idComment}")
    Call<TaskDto> updateComment(@Header("Authorization") String token, @Path("id") String id, @Path("idComment") String idComment);

    @DELETE("api/comments/delete/{idTask}/{idUser}/{idComment}")
    Call<Void> deleteComment(@Header("Authorization") String token, @Path("idTask") String idTask, @Path("idUser") String idUser, @Path("idComment") String idComment);
}