package com.diploma.assistant.service.connection;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.diploma.assistant.controller.ChatController;
import com.diploma.assistant.model.entity.resource_service.Chat;
import com.diploma.assistant.model.enumaration.URIEnum;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatService {
    private final MutableLiveData<Boolean> booleanMlD = new MutableLiveData<>();
    private final MutableLiveData<Chat> chatMlD = new MutableLiveData<>();
    private final MutableLiveData<List<Chat>> chatListMlD = new MutableLiveData<>();

    ChatController service =  RetrofitService.getRetrofit(URIEnum.BASE_URL.getUrl()).create(ChatController.class);

    public MutableLiveData<List<Chat>> getAllMessageForChat(String token, String userId) {
        Call<List<Chat>> call = service.getAllMessageForChat(token, userId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Chat>> call, @NonNull Response<List<Chat>> response) {
                if(response.isSuccessful()){
                    chatListMlD.setValue(response.body());
                } else {
                    chatListMlD.postValue(null);
                    Log.e("GET MESSAGES FOR CHAT", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Chat>> call, @NonNull Throwable t) {
                chatListMlD.postValue(null);
                Log.e("GET MESSAGES FOR CHAT", t.getMessage(), t.fillInStackTrace());
            }
        });
        return chatListMlD;
    }

    public MutableLiveData<Chat> createChatMessage(String token, Chat chat) {
        Call<Chat> call = service.createChatMessage(token, chat);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Chat> call, @NonNull Response<Chat> response) {
                if(response.isSuccessful()){
                    chatMlD.setValue(response.body());
                } else {
                    chatMlD.postValue(null);
                    Log.e("CREATE MESSAGE FOR CHAT", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Chat> call, @NonNull Throwable t) {
                chatMlD.postValue(null);
                Log.e("CREATE MESSAGE FOR CHAT", t.getMessage(), t.fillInStackTrace());
            }
        });
        return chatMlD;
    }

    public MutableLiveData<Chat> updateChatMessage(String token, String messageId, Chat chat) {
        Call<Chat> call = service.updateChatMessage(token, messageId, chat);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Chat> call, @NonNull Response<Chat> response) {
                if(response.isSuccessful()){
                    chatMlD.setValue(response.body());
                } else {
                    chatMlD.postValue(null);
                    Log.e("CREATE MESSAGE FOR CHAT", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Chat> call, @NonNull Throwable t) {
                chatMlD.postValue(null);
                Log.e("CREATE MESSAGE FOR CHAT", t.getMessage(), t.fillInStackTrace());
            }
        });
        return chatMlD;
    }

    public MutableLiveData<Boolean> deleteChatMessageById(String token, String id) {
        Call<Void> call = service.deleteChatMessageById(token, id);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    booleanMlD.setValue(true);
                } else {
                    booleanMlD.postValue(false);
                    Log.e("DELETE MESSAGE FROM CHAT", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                booleanMlD.postValue(false);
                Log.e("DELETE MESSAGE FROM CHAT", t.getMessage(), t.fillInStackTrace());
            }
        });
        return booleanMlD;
    }
}