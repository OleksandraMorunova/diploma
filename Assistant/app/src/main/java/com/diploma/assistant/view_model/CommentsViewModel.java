package com.diploma.assistant.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.diploma.assistant.model.entity.resource_service.CommentsDto;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.service.connection.CommentService;

public class CommentsViewModel extends AndroidViewModel {
    CommentService repository = new CommentService();

    public CommentsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<TaskDto> postComment(String token, String id, CommentsDto comment){
        return repository.postComment(token, id, comment);
    }

    public LiveData<TaskDto> updateComment(String token, String id, String idComment){
        return repository.updateComment(token, id, idComment);
    }

    public LiveData<Boolean> deleteComment(String token, String idTask, String idUser, String idComment){
        return repository.deleteComment(token, idTask, idUser, idComment);
    }
}