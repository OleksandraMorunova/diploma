package com.diploma.assistant.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.service.connection.TasksService;

import java.util.List;

import okhttp3.MultipartBody;

public class TasksViewModel extends AndroidViewModel {
    TasksService repository = new  TasksService();

    public TasksViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<TaskDto>> getTaskDtoMutableLiveData(String token){
        return repository.getComments(token);
    }

    public LiveData<TaskDto> getTaskByIDTask(String token, String idTask){
        return repository.getTaskByIDTask(token, idTask);
    }

    public LiveData<Boolean> createTask(String token, TaskDto taskDto, List<MultipartBody.Part> file){
        return repository.createTask(token, taskDto, file);
    }

    public LiveData<Boolean> deleteTask(String token, String idTask){
        return repository.deleteTask(token, idTask);
    }

    public LiveData<TaskDto> updateTask(String token, String idTask, TaskDto taskDto, List<MultipartBody.Part> file){
        return repository.updateTask(token, idTask,taskDto, file);
    }
}