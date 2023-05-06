package com.diploma.assistant.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.service.connection.TasksService;

import java.util.List;

public class TasksViewModel extends AndroidViewModel {
    TasksService repository = new  TasksService();

    public TasksViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<TaskDto>> getTaskDtoMutableLiveData(String token){
        return repository.getTaskDtoMutableLiveData(token);
    }


}
