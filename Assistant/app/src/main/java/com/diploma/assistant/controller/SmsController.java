package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.registration_service.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface SmsController {
    @PUT("otc/generated/code")
    Call<ResponseBody> createCode(@Body User user);

    @POST("otc/get/code")
    Call<ResponseBody> getCode(@Body User user);

    @POST("otc/sms")
    Call<ResponseBody> postSms(@Body User user);
}
