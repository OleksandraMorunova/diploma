package com.diploma.assistant.service.connection;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.entity.regiastartion_and_resource_services.UserAndTasks;
import com.diploma.assistant.model.entity.regiastartion_and_resource_services.UsersAndCountTasks;
import com.diploma.assistant.model.enumaration.URIEnum;
import com.diploma.assistant.controller.UserController;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class UserService {
    private final MutableLiveData<ResponseBody>  mutableLiveDataString = new MutableLiveData<>();
    private final MutableLiveData<UserAndTasks> taskMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<UsersAndCountTasks> countTasksMutableLiveData = new MutableLiveData<>();

    UserController service = RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl()).create(UserController.class);

    public MutableLiveData<User> createUser(String token, User user){
        Call<User> call = service.createUser(token, user);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userMutableLiveData.setValue(response.body());
                } else{
                    Log.e("CREATE USER", "Code: " + response.code());
                    userMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                userMutableLiveData.postValue(null);
                Log.e("CREATE USER", t.getMessage(), t.fillInStackTrace());
            }
        });
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> deleteUserById(String token, String id){
        Call<Void> call = service.deleteUserById(token, id);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()){
                    booleanMutableLiveData.setValue(true);
                } else {
                    Log.e("DELETE USER", "Code: " + response.code());
                    booleanMutableLiveData.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                booleanMutableLiveData.postValue(false);
                Log.e("DELETE USER", t.getMessage(), t.fillInStackTrace());
            }
        });
        return booleanMutableLiveData;
    }

    public MutableLiveData<UserAndTasks> getDetailsUser(String email, String token){
        Call<UserAndTasks> call = service.getDetails(email, token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserAndTasks> call, @NonNull Response<UserAndTasks> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getUserDto() != null) {
                    taskMutableLiveData.setValue(response.body());
                } else{
                    Log.e("GET USER`S DATA", "Code: " + response.code());
                    taskMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserAndTasks> call, @NonNull Throwable t) {
                Log.e("GET USER`S DATA", t.getMessage(), t.fillInStackTrace());
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
                } else {
                    Log.e("GET USERS", "Code: " + response.code());
                    countTasksMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UsersAndCountTasks> call, @NonNull Throwable t) {
                Log.e("GET USERS", t.getMessage(), t.fillInStackTrace());
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
                } else {
                    Log.e("CHECK USER EMAIL", "Code: " + response.code());
                    mutableLiveDataString.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("CHECK USER EMAIL", t.getMessage(), t.fillInStackTrace());
                mutableLiveDataString.postValue(null);
            }
        });
        return mutableLiveDataString;
    }

    public MutableLiveData<User> checkUserId(String id){
        Call<User> call = service.checkUserId(id);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    userMutableLiveData.setValue(response.body());
                } else {
                    Log.e("CHECK USER ID", "Code: " + response.code());
                    userMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("CHECK USER ID", t.getMessage(), t.fillInStackTrace());
                userMutableLiveData.postValue(null);
            }
        });
        return userMutableLiveData;
    }

    public MutableLiveData<ResponseBody> getPhoneNumber(String phone){
        Call<ResponseBody> call = service.checkUserPhone(phone);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mutableLiveDataString.setValue(response.body());
                } else {
                    Log.e("GET USER`S PHONE NUMBER", "Code: " + response.code());
                    mutableLiveDataString.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("GET USER`S PHONE NUMBER", t.getMessage(), t.fillInStackTrace());
                mutableLiveDataString.postValue(null);
            }
        });
        return mutableLiveDataString;
    }

    public MutableLiveData<User> updateUserDetails(String phone, User user, MultipartBody.Part file){
        Call<User> call = service.updateUserDetails(phone, user, file);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userMutableLiveData.setValue(response.body());
                } else {
                    Log.e("UPDATE USER`S DATA", "Code: " + response.code());
                    userMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("UPDATE USER`S DATA", t.getMessage(), t.fillInStackTrace());
                userMutableLiveData.postValue(null);
            }
        });
        return userMutableLiveData;
    }
}