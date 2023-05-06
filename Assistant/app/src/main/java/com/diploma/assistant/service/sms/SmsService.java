package com.diploma.assistant.service.sms;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.controller.SmsController;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.enumaration.URIEnum;
import com.diploma.assistant.service.connection.RetrofitService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsService {
    private final MutableLiveData<ResponseBody>  mutableLiveDataString = new MutableLiveData<>();
    SmsController service = RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl()).create(SmsController.class);

    public MutableLiveData<ResponseBody> createCode(User user){
        Call<ResponseBody> call = service.createCode(user);
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

    public MutableLiveData<ResponseBody> getCode(User user){
        Call<ResponseBody> call = service.getCode(user);
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

    public MutableLiveData<ResponseBody> postSms(User user){
        Call<ResponseBody> call = service.postSms(user);
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
