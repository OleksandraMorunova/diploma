package com.diploma.assistant.service.connection;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.entity.regiastartion_and_resource_services.UserAndTasks;
import com.diploma.assistant.model.entity.regiastartion_and_resource_services.UsersAndCountTasks;
import com.diploma.assistant.model.enumaration.URIEnum;
import com.diploma.assistant.controller.UserController;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {
    private final MutableLiveData<ResponseBody>  mutableLiveDataString = new MutableLiveData<>();
    private final MutableLiveData<UserAndTasks> taskMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<UsersAndCountTasks> countTasksMutableLiveData = new MutableLiveData<>();

    UserController service = RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl()).create(UserController.class);

    public MutableLiveData<User> createUser(String token, User user){
        Call<User> call = service.createUser(token, user);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userMutableLiveData.setValue(response.body());
                } else userMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                userMutableLiveData.postValue(null);
            }
        });
        return userMutableLiveData;
    }

    public MutableLiveData<UserAndTasks> getDetailsUser(String email, String token){
        Call<UserAndTasks> call = service.getDetails(email, token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserAndTasks> call, @NonNull Response<UserAndTasks> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getUserDto() != null) {
                    taskMutableLiveData.setValue(response.body());
                } else taskMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<UserAndTasks> call, @NonNull Throwable t) {
                Log.e("TAG", "Get user data", t.getCause());
                taskMutableLiveData.postValue(null);
            }
        });
        return taskMutableLiveData;
    }

    public MutableLiveData<UsersAndCountTasks> getUsers(String token){
        Call<UsersAndCountTasks> call = service.getUsers(token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UsersAndCountTasks> call, @NonNull Response<UsersAndCountTasks> response) {
                if (response.isSuccessful() && response.body() != null) {
                    countTasksMutableLiveData.setValue(response.body());
                } else countTasksMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<UsersAndCountTasks> call, @NonNull Throwable t) {
                Log.e("TAG", "List users", t.getCause());
                countTasksMutableLiveData.postValue(null);
            }
        });
        return countTasksMutableLiveData;
    }

    public MutableLiveData<ResponseBody> checkUserEmail(String email){
        Call<ResponseBody> call = service.checkUserEmail(email);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    mutableLiveDataString.setValue(response.body());
                } else mutableLiveDataString.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("TAG", "Check user email", t.getCause());
                mutableLiveDataString.postValue(null);
            }
        });
        return mutableLiveDataString;
    }

    public MutableLiveData<ResponseBody> getPhoneNumber(String phone){
        Call<ResponseBody> call = service.checkUserPhone(phone);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mutableLiveDataString.setValue(response.body());
                } else mutableLiveDataString.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("TAG", "Get user phone number", t.getCause());
                mutableLiveDataString.postValue(null);
            }
        });
        return mutableLiveDataString;
    }

    public MutableLiveData<ResponseBody> updateUserDetails(String phone, User user){
        Call<ResponseBody> call = service.updateUserDetails(phone, user);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mutableLiveDataString.setValue(response.body());
                } else mutableLiveDataString.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("TAG", "Update user data", t.getCause());
                mutableLiveDataString.postValue(null);
            }
        });
        return mutableLiveDataString;
    }
}
