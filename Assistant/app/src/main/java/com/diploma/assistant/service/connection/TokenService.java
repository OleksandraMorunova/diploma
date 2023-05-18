package com.diploma.assistant.service.connection;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.controller.TokenController;
import com.diploma.assistant.model.entity.registration_service.LoginRequest;
import com.diploma.assistant.model.entity.registration_service.LoginResponse;
import com.diploma.assistant.model.entity.registration_service.RefreshJwtRequest;
import com.diploma.assistant.model.enumaration.URIEnum;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenService {
    private final MutableLiveData<LoginResponse> mutableLiveData = new MutableLiveData<>();
    TokenController service = RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl()).create(TokenController.class);

    public MutableLiveData<LoginResponse> getToken(String email, String password){
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = service.login(loginRequest);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mutableLiveData.setValue(response.body());
                } else {
                    Log.e("ACCESS_TOKEN", "Code: " + response.code());
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.e("ACCESS_TOKEN", t.getMessage(), t.fillInStackTrace());
                mutableLiveData.postValue(null);
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<LoginResponse> getNewAccessToken(String refreshToken){
        RefreshJwtRequest loginRequest = new RefreshJwtRequest(refreshToken);
        Call<LoginResponse> call = service.refresh(loginRequest);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mutableLiveData.setValue(response.body());
                } else {
                    Log.e("NEW_ACCESS_TOKEN", "Code: " + response.code());
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.e("NEW_ACCESS_TOKEN", t.getMessage(), t.fillInStackTrace());
                mutableLiveData.postValue(null);
            }
        });
        return mutableLiveData;
    }
}
