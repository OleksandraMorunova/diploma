package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.regiastartion_and_resource_services.UserAndTasks;
import com.diploma.assistant.model.entity.regiastartion_and_resource_services.UsersAndCountTasks;
import com.diploma.assistant.model.entity.registration_service.LoadFile;
import com.diploma.assistant.model.entity.registration_service.User;

import org.bson.types.ObjectId;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FilesController {
    @GET("api/files/download/{idFiles}")
    Call<LoadFile> getFiles(@Header("Authorization") String token, @Path("idFiles") String idFiles);
}
