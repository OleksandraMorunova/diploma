package com.diploma.assistant.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.diploma.assistant.model.entity.resource_service.ResponseTask;
import com.diploma.assistant.service.connection.TaskResponseService;

import java.util.List;

import okhttp3.MultipartBody;

public class TaskResponseViewModel extends AndroidViewModel {
    TaskResponseService repository = new  TaskResponseService();

    public TaskResponseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ResponseTask> getAllTaskResponseByIdUser(String token, String idTask){
        return repository.getTaskResponseByIdUser(token, idTask);
    }
}
