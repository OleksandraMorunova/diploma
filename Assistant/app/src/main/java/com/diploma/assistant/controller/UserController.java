package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.LoginRequest;
import com.diploma.assistant.model.entity.LoginResponse;
import com.diploma.assistant.model.entity.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserController {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("api/content/user/{email}")
    Call<User> getDetails(@Path("email") String email, @Header("Authorization") String token);

    @GET("api/content/phone/{phone}")
    Call<ResponseBody> getPhoneNumber(@Path("phone") String phone);

    @PUT("api/content/update/{phone}")
    Call<ResponseBody> updateUserDetails(@Path("phone") String phone, @Body User user);

    @PUT("otc/generated/code")
    Call<ResponseBody> createCode(@Body User phone);

    @POST("otc/get/code")
    Call<ResponseBody> getCode(@Body User code);

    @POST("otc/sms")
    Call<ResponseBody> postSms(@Body User phone);

}
