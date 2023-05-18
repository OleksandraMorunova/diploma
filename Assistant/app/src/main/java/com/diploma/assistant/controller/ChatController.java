package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.resource_service.Chat;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ChatController {
    @GET("api/chat/get/{userId}")
    Call<List<Chat>> getAllMessageForChat(@Header("Authorization") String token, @Path("userId") String userId);

    @POST("api/chat/create")
    Call<Chat> createChatMessage(@Header("Authorization") String token, @Body Chat chat);

    @PUT("api/chat/update/{messageId}")
    Call<Chat> updateChatMessage(@Header("Authorization") String token, @Path("messageId") String userId, @Body Chat chat);

    @DELETE("api/chat/delete/{id}")
    Call<Void> deleteChatMessageById(@Header("Authorization") String token, @Path("id") String id);
}
