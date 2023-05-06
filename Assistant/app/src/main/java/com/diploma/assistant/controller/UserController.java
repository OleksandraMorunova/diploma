package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.entity.regiastartion_and_resource_services.UserAndTasks;
import com.diploma.assistant.model.entity.regiastartion_and_resource_services.UsersAndCountTasks;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserController {
    @GET("api/content/user/{email}")
    Call<UserAndTasks> getDetails(@Path("email") String email, @Header("Authorization") String token);

    @GET("api/content/check/email/{email}")
    Call<ResponseBody> checkUserEmail(@Path("email") String email);

    @GET("api/content/check/phone/{phone}")
    Call<ResponseBody> checkUserPhone(@Path("phone") String phone);

    @PUT("api/content/update/{phone}")
    Call<ResponseBody> updateUserDetails(@Path("phone") String phone, @Body User user);

    @POST("api/admin/create")
    Call<User> createUser(@Header("Authorization") String token, @Body User user);

    @GET("api/admin/list/users")
    Call<UsersAndCountTasks> getUsers(@Header("Authorization") String token);
}
