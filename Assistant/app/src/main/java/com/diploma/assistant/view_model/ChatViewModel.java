package com.diploma.assistant.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.diploma.assistant.model.entity.resource_service.Chat;
import com.diploma.assistant.service.connection.ChatService;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    ChatService repository = new ChatService();

    public ChatViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Chat> createChatMessage(String token, Chat chat){
        return repository.createChatMessage(token, chat);
    }

    public LiveData<Chat> updateChatMessage(String token, String messageId, Chat chat){
        return repository.updateChatMessage(token, messageId, chat);
    }

    public LiveData<List<Chat>> getAllMessageForChat(String token, String userId){
        return repository.getAllMessageForChat(token, userId);
    }

    public LiveData<Boolean> deleteChatMessageById(String token, String id){
        return repository.deleteChatMessageById(token, id);
    }
}