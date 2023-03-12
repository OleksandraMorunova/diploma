package com.diploma.assistant.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.model.entity.LoginRequest;
import com.diploma.assistant.model.entity.LoginResponse;
import com.diploma.assistant.model.entity.User;
import com.diploma.assistant.model.enumaration.URIEnum;
import com.diploma.assistant.controller.UserController;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {
   private MutableLiveData<User> mutableLiveDataUser = new MutableLiveData<>();
   private MutableLiveData<LoginResponse> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseBody>  mutableLiveDataString = new MutableLiveData<>();

    UserController service = RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl());

    public MutableLiveData<LoginResponse> getToken(String email, String password){
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = service.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                System.out.println(response.body() + " " + response.code());
                if(response.isSuccessful() && response.body() != null){
                    mutableLiveData.setValue(response.body());
                } else  mutableLiveData.postValue(null);
            }
            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                mutableLiveData.postValue(null);
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<User> getDetailsUser(String email, String token){
        Call<User> call = service.getDetails(email, token);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if(response.isSuccessful()) {
                    mutableLiveDataUser.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                mutableLiveDataUser.postValue(null);
            }
        });
        return mutableLiveDataUser;
    }

    public MutableLiveData<ResponseBody> getPhoneNumber(String phone){
        Call<ResponseBody> call = service.getPhoneNumber(phone);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null) {
                    mutableLiveDataString.setValue(response.body());
                } else mutableLiveDataString.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                mutableLiveDataString.postValue(null);
            }
        });
        return mutableLiveDataString;
    }

    public MutableLiveData<ResponseBody> createCode(User phone){
        Call<ResponseBody> call = service.createCode(phone);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    mutableLiveDataString.setValue(response.body());
                } else mutableLiveDataString.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                mutableLiveDataString.postValue(null);
            }
        });
        return mutableLiveDataString;
    }

    public MutableLiveData<ResponseBody> getCode(User code){
        Call<ResponseBody> call = service.getCode(code);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null) {
                    mutableLiveDataString.setValue(response.body());
                } else mutableLiveDataString.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                mutableLiveDataString.postValue(null);
            }
        });
        return mutableLiveDataString;
    }

    public MutableLiveData<ResponseBody> postSms(User phone){
        Call<ResponseBody> call = service.postSms(phone);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null) {
                    mutableLiveDataString.setValue(response.body());
                } else mutableLiveDataString.postValue(null);
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                mutableLiveDataString.postValue(null);
            }
        });
        return mutableLiveDataString;
    }

    public MutableLiveData<ResponseBody> updateUserDetails(String phone, User user){
        Call<ResponseBody> call = service.updateUserDetails(phone, user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null) {
                    mutableLiveDataString.setValue(response.body());
                } else mutableLiveDataString.postValue(null);
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                mutableLiveDataString.postValue(null);
            }
        });
        return mutableLiveDataString;
    }
}
