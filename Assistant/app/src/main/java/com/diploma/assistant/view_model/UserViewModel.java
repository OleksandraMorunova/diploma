package com.diploma.assistant.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.*;

import com.diploma.assistant.model.entity.*;
import com.diploma.assistant.repository.UserService;

import okhttp3.ResponseBody;

public class UserViewModel extends AndroidViewModel {
    UserService repository = new UserService();

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<LoginResponse> getToken(String email, String password){
        return repository.getToken(email, password);
    }

    public LiveData<User> getDetailsByEmail(String token, String email){
        return repository.getDetailsUser(token, email);
    }

    public LiveData<ResponseBody> getDetailsByPhone(String phoneNumber){
        return repository.getPhoneNumber(phoneNumber);
    }

    public LiveData<ResponseBody> createCode(User phone){
        return repository.createCode(phone);
    }

    public LiveData<ResponseBody> getCode(User code){
        return repository.getCode(code);
    }

    public LiveData<ResponseBody> postSms(User phone) {
        return repository.postSms(phone);
    }

    public LiveData<ResponseBody> updateUserDetails(String phone, User user) {
        return repository.updateUserDetails(phone, user);
    }
}
