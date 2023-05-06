package com.diploma.assistant.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.*;

import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.entity.regiastartion_and_resource_services.UserAndTasks;
import com.diploma.assistant.model.entity.regiastartion_and_resource_services.UsersAndCountTasks;
import com.diploma.assistant.service.connection.UserService;

import okhttp3.ResponseBody;

public class UserViewModel extends AndroidViewModel {
    UserService repository = new UserService();

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<User> createUser(String token, User user){
        return repository.createUser(token, user);
    }

    public LiveData<UserAndTasks> getDetailsUser(String token, String email){
        return repository.getDetailsUser(token, email);
    }

    public LiveData<ResponseBody> checkUserEmail(String email) {
        return repository.checkUserEmail(email);
    }
    public LiveData<ResponseBody> getDetailsByPhone(String phoneNumber){
        return repository.getPhoneNumber(phoneNumber);
    }

    public LiveData<ResponseBody> updateUserDetails(String phone, User user) {
        return repository.updateUserDetails(phone, user);
    }

    public LiveData<UsersAndCountTasks> getUsers(String token){
        return repository.getUsers(token);
    }
}
