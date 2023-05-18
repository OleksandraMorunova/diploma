package com.diploma.assistant.controller;

import com.diploma.assistant.model.entity.registration_service.LoadFile;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface FilesController {
    @GET("api/files/download/{idFiles}")
    Call<LoadFile> getFiles(@Header("Authorization") String token, @Path("idFiles") String idFiles);

    @GET("api/v1/files/download/{idFiles}")
    Call<LoadFile> getListFiles(@Header("Authorization") String token, @Path("idFiles") String idFiles);
}