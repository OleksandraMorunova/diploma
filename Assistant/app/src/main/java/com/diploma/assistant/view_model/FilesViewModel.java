package com.diploma.assistant.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.diploma.assistant.model.entity.registration_service.LoadFile;
import com.diploma.assistant.model.entity.registration_service.LoginResponse;
import com.diploma.assistant.service.connection.FilesService;
import com.diploma.assistant.service.connection.TokenService;

import org.bson.types.ObjectId;

public class FilesViewModel extends AndroidViewModel {
    FilesService repository = new FilesService();

    public FilesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<LoadFile> getFiles(String token, String idFiles){
        return repository.getFiles(token, idFiles);
    }
}
