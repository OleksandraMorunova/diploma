package com.diploma.assistant.service.sms;

import android.util.Log;

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
    private final MutableLiveData<User>  mutableLiveDataString = new MutableLiveData<>();
    private final MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
    SmsController service = RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl()).create(SmsController.class);

    public MutableLiveData<User> createCode(User user){
        Call<User> call = service.createCode(user);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if(response.isSuccessful()) {
                    mutableLiveDataString.setValue(response.body());
                } else {
                    Log.e("CREATE SMS", "Code: " + response.code());
                    mutableLiveDataString.postValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("CREATE SMS", t.getMessage(), t.fillInStackTrace());
                mutableLiveDataString.postValue(null);
            }
        });
        return mutableLiveDataString;
    }

    public MutableLiveData<Boolean> getCode(String user){
        Call<Void> call = service.getCode(user);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()) {
                    booleanMutableLiveData.setValue(true);
                } else {
                    Log.e("GET SMS", "Code: " + response.code());
                    booleanMutableLiveData.postValue(false);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("GET CODE", t.getMessage(), t.fillInStackTrace());
                booleanMutableLiveData.postValue(false);
            }
        });
        return booleanMutableLiveData;
    }

    public MutableLiveData<User> postSms(User user){
        Call<User> call = service.postSms(user);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if(response.isSuccessful() && response.body() != null) {
                    mutableLiveDataString.setValue(response.body());
                } else {
                    Log.e("POST SMS", "Code: " + response.code());
                    mutableLiveDataString.postValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("POST SMS", t.getMessage(), t.fillInStackTrace());
                mutableLiveDataString.postValue(null);
            }
        });
        return mutableLiveDataString;
    }
}
