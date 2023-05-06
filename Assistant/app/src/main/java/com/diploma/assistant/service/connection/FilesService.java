package com.diploma.assistant.service.connection;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.controller.FilesController;
import com.diploma.assistant.controller.TokenController;
import com.diploma.assistant.model.entity.registration_service.LoadFile;
import com.diploma.assistant.model.entity.registration_service.LoginRequest;
import com.diploma.assistant.model.entity.registration_service.LoginResponse;
import com.diploma.assistant.model.entity.registration_service.RefreshJwtRequest;
import com.diploma.assistant.model.enumaration.URIEnum;

import org.bson.types.ObjectId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilesService {
    private final MutableLiveData<LoadFile> mutableLiveData = new MutableLiveData<>();
    FilesController service = RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl()).create(FilesController.class);

    public MutableLiveData<LoadFile> getFiles(String token, String idFiles){
        Call<LoadFile> call = service.getFiles(token, idFiles);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoadFile> call, @NonNull Response<LoadFile> response) {
                System.out.println(response.body() + "  " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    mutableLiveData.setValue(response.body());
                } else mutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<LoadFile> call, @NonNull Throwable t) {
                t.printStackTrace();
                mutableLiveData.postValue(null);
            }
        });
        return mutableLiveData;
    }
}
