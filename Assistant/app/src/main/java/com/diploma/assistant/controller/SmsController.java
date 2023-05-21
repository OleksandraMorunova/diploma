package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.registration_service.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SmsController {
    @PUT("otc/generated/code")
    Call<User> createCode(@Body User user);

    @DELETE("otc/get/code/{code}")
    Call<Void> getCode(@Path("code") String code);

    @POST("otc/sms")
    Call<User> postSms(@Body User user);
}
