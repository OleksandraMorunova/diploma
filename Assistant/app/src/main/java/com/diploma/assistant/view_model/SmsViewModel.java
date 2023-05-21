package com.diploma.assistant.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.service.sms.SmsService;

import okhttp3.ResponseBody;

public class SmsViewModel extends AndroidViewModel {
    SmsService repository = new SmsService();

    public SmsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<User> createCode(User user){
        return repository.createCode(user);
    }

    public LiveData<Boolean> getCode(String user){
        return repository.getCode(user);
    }

    public LiveData<User> postSms(User user) {
        return repository.postSms(user);
    }
}
