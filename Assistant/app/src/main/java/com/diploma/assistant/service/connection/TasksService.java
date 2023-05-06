package com.diploma.assistant.service.connection;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.controller.TasksController;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.model.enumaration.URIEnum;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksService {
    private final MutableLiveData<List<TaskDto>> taskDtoMutableLiveData = new MutableLiveData<>();

    TasksController service = RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl()).create(TasksController.class);

    public MutableLiveData<List<TaskDto>> getTaskDtoMutableLiveData(String token) {
        Call<List<TaskDto>> call = service.getComments(token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<TaskDto>> call, @NonNull Response<List<TaskDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    taskDtoMutableLiveData.setValue(response.body());
                } else taskDtoMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<List<TaskDto>> call, @NonNull Throwable t) {
                taskDtoMutableLiveData.postValue(null);
            }
        });
        return taskDtoMutableLiveData;
    }
}
