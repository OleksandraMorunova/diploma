package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.registration_service.LoginRequest;
import com.diploma.assistant.model.entity.registration_service.LoginResponse;
import com.diploma.assistant.model.entity.registration_service.RefreshJwtRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TokenController {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/refresh")
    Call<LoginResponse> refresh(@Body RefreshJwtRequest refreshToken);
}
