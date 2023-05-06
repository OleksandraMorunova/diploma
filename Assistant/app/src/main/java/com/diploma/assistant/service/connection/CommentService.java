package com.diploma.assistant.service.connection;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.controller.CommentsController;
import com.diploma.assistant.controller.TasksController;
import com.diploma.assistant.model.entity.resource_service.CommentsDto;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.model.enumaration.URIEnum;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentService {
    private final MutableLiveData<TaskDto> taskDtoMutableLiveData = new MutableLiveData<>();

    CommentsController service = RetrofitService.getRetrofit(URIEnum.RESOURCE_URL.getUrl()).create(CommentsController.class);

    public MutableLiveData<TaskDto> postComment(String id, CommentsDto comment) {
        Call<TaskDto> call = service.postComment(id, comment);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TaskDto> call, @NonNull Response<TaskDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    taskDtoMutableLiveData.setValue(response.body());
                } else taskDtoMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<TaskDto> call, @NonNull Throwable t) {
                taskDtoMutableLiveData.postValue(null);
            }
        });
        return taskDtoMutableLiveData;
    }

    public MutableLiveData<TaskDto> updateComment(String id, String idComment) {
        Call<TaskDto> call = service.updateComment(id, idComment);
        System.out.println(id + " " + idComment);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TaskDto> call, @NonNull Response<TaskDto> response) {
                System.out.println(response.body() + " " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    taskDtoMutableLiveData.setValue(response.body());
                } else taskDtoMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<TaskDto> call, @NonNull Throwable t) {
                t.printStackTrace();
                taskDtoMutableLiveData.postValue(null);
            }
        });
        return taskDtoMutableLiveData;
    }
}
