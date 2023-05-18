package com.diploma.assistant.service.connection;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.controller.TasksResponseController;
import com.diploma.assistant.model.entity.resource_service.ResponseTask;
import com.diploma.assistant.model.enumaration.URIEnum;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskResponseService {
    private final MutableLiveData<ResponseTask> mutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> vData = new MutableLiveData<>();
    TasksResponseController service = RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl()).create(TasksResponseController.class);

    public MutableLiveData<ResponseTask> getTaskResponseByIdUser(String token, String idTask){
        Call<ResponseTask> call = service.getAllTaskResponseByIdUser(token, idTask);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseTask> call, @NonNull Response<ResponseTask> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mutableLiveData.setValue(response.body());
                } else mutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseTask> call, @NonNull Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<Boolean> createTaskResponse(String token, ResponseTask responseTask, List<MultipartBody.Part> file){
        Call<Void> call = service.createTaskResponse(token, responseTask, file);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    vData.setValue(true);
                } else  vData.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                vData.postValue(false);
            }
        });
        return vData;
    }
}
