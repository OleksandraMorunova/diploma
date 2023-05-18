package com.diploma.assistant.service.connection;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.controller.TasksController;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.model.enumaration.URIEnum;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksService {
    private final MutableLiveData<List<TaskDto>> mTaskList = new MutableLiveData<>();
    private final MutableLiveData<TaskDto> mTask = new MutableLiveData<>();
    private final MutableLiveData<Boolean> vTask = new MutableLiveData<>();

    TasksController service = RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl()).create(TasksController.class);

    public MutableLiveData<List<TaskDto>> getComments(String token) {
        Call<List<TaskDto>> call = service.getComments(token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<TaskDto>> call, @NonNull Response<List<TaskDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mTaskList.setValue(response.body());
                } else {
                    Log.e("GET COMMENTS", "Code: " + response.code());
                    mTaskList.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TaskDto>> call, @NonNull Throwable t) {
                Log.e("GET COMMENTS", t.getMessage(), t.fillInStackTrace());
                mTaskList.postValue(null);
            }
        });
        return mTaskList;
    }

    public MutableLiveData<TaskDto> getTaskByIDTask(String token, String idTask) {
        Call<TaskDto> call = service.getTaskByIDTask(token, idTask);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TaskDto> call, @NonNull Response<TaskDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mTask.setValue(response.body());
                } else{
                    Log.e("UPDATE TASK", "Code: " + response.code());
                    mTask.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TaskDto> call, @NonNull Throwable t) {
                Log.e("UPDATE TASK", t.getMessage(), t.fillInStackTrace());
                mTask.postValue(null);
            }
        });
        return mTask;
    }

    public MutableLiveData<List<TaskDto>> getTaskByIDUser(String token, String idUser) {
        Call<List<TaskDto>> call = service.getAllTasksByIdUser(token, idUser);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<TaskDto>> call, @NonNull Response<List<TaskDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mTaskList.setValue(response.body());
                } else {
                    Log.e("GET TASK BY ID USER", "Code: " + response.code());
                    mTaskList.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TaskDto>> call, @NonNull Throwable t) {
                Log.e("GET TASK BY ID USER", t.getMessage(), t.fillInStackTrace());
                mTaskList.postValue(null);
            }
        });
        return mTaskList;
    }

    public MutableLiveData<Boolean> createTask(String token, TaskDto taskDto, List<MultipartBody.Part> file){
        Call<Void> call = service.createTask(token, taskDto, file);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    vTask.setValue(true);
                } else {
                    Log.e("CREATE TASK", "Code: " + response.code());
                    vTask.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("CREATE TASK", t.getMessage(), t.fillInStackTrace());
                vTask.postValue(false);
            }
        });
        return vTask;
    }

    public MutableLiveData<Boolean> deleteTask(String token, String idTask){
        Call<Void> call = service.deleteTask(token, idTask);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    vTask.setValue(true);
                } else {
                    Log.e("DELETE TASK", "Code: " + response.code());
                    vTask.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("CREATE TASK", t.getMessage(), t.fillInStackTrace());
                vTask.postValue(false);
            }
        });
        return vTask;
    }

    public MutableLiveData<TaskDto> updateTask(String token, String idTask, TaskDto taskDto, List<MultipartBody.Part> file){
        Call<TaskDto> call = service.updateTask(token, idTask,taskDto, file);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TaskDto> call, @NonNull Response<TaskDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mTask.setValue(response.body());
                } else {
                    Log.e("UPDATE TASK", "Code: " + response.code());
                    mTask.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TaskDto> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.e("UPDATE TASK", t.getMessage(), t.fillInStackTrace());
                mTask.postValue(null);
            }
        });
        return mTask;
    }
}