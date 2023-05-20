package com.diploma.assistant.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.diploma.assistant.model.entity.registration_service.LoginResponse;
import com.diploma.assistant.service.connection.TokenService;

public class TokenViewModel extends AndroidViewModel {
    TokenService repository = new TokenService();

    public TokenViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<LoginResponse> getToken(String email, String password){
        return repository.getToken(email, password);
    }

    public LiveData<LoginResponse> getNewAccessAndRefreshToken(String refreshToken){
        return repository.getNewAccessAndRefreshToken(refreshToken);
    }
}
