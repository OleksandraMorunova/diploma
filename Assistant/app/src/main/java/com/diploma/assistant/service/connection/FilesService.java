package com.diploma.assistant.service.connection;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.controller.FilesController;
import com.diploma.assistant.model.entity.registration_service.LoadFile;
import com.diploma.assistant.model.enumaration.URIEnum;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilesService {
    private final MutableLiveData<LoadFile> loadFiles = new MutableLiveData<>();
    FilesController service = RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl()).create(FilesController.class);

    public MutableLiveData<LoadFile> getFiles(String token, String idFiles){
        Call<LoadFile> call = service.getFiles(token, idFiles);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoadFile> call, @NonNull Response<LoadFile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loadFiles.setValue(response.body());
                } else {
                    loadFiles.postValue(null);
                    Log.e("GET FILE`S BYTES", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoadFile> call, @NonNull Throwable t) {
                loadFiles.postValue(null);
                t.printStackTrace();
                Log.e("GET FILE`S BYTES", t.getMessage(), t.fillInStackTrace());
            }
        });
        return loadFiles;
    }

    public MutableLiveData<LoadFile> getListFiles(String token, String idFiles){
        Call<LoadFile> call = service.getListFiles(token, idFiles);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoadFile> call, @NonNull Response<LoadFile> response) {
                System.out.println(response.code() + " " + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    loadFiles.setValue(response.body());
                } else {
                    loadFiles.postValue(null);
                    Log.e("GET FILE`S BYTES", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoadFile> call, @NonNull Throwable t) {
                loadFiles.postValue(null);
                Log.e("GET FILE`S BYTES", t.getMessage(), t.fillInStackTrace());
            }
        });
        return loadFiles;
    }
}