package com.diploma.assistant.service.connection;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.controller.CommentsController;
import com.diploma.assistant.model.entity.resource_service.CommentsDto;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.model.enumaration.URIEnum;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentService {
    private final MutableLiveData<TaskDto> task = new MutableLiveData<>();
    private final MutableLiveData<Boolean> booleanMlD = new MutableLiveData<>();

    CommentsController service = RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl()).create(CommentsController.class);

    public MutableLiveData<TaskDto> postComment(String token, String id, CommentsDto comment) {
        Call<TaskDto> call = service.postComment(token, id, comment);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TaskDto> call, @NonNull Response<TaskDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    task.setValue(response.body());
                } else {
                    Log.e("CREATE COMMENT", "Code: " + response.code());
                    task.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TaskDto> call, @NonNull Throwable t) {
                Log.e("CREATE COMMENT", t.getMessage(), t.fillInStackTrace());
                task.postValue(null);
            }
        });
        return task;
    }

    public MutableLiveData<TaskDto> updateComment(String token, String id, String idComment) {
        Call<TaskDto> call = service.updateComment(token, id, idComment);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TaskDto> call, @NonNull Response<TaskDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    task.setValue(response.body());
                } else {
                    Log.e("UPDATE COMMENT", "Code: " + response.code());
                    task.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TaskDto> call, @NonNull Throwable t) {
                Log.e("UPDATE COMMENT", t.getMessage(), t.fillInStackTrace());
                task.postValue(null);
            }
        });
        return task;
    }

    public MutableLiveData<Boolean> deleteComment(String token, String idTask, String idUser, String idComment) {
        Call<Void> call = service.deleteComment(token, idTask, idUser, idComment);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    booleanMlD.setValue(true);
                } else {
                    Log.e("DELETE COMMENT", "Code: " + response.code());
                    booleanMlD.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("DELETE COMMENT", t.getMessage(), t.fillInStackTrace());
                booleanMlD.postValue(false);
            }
        });
        return booleanMlD;
    }
}