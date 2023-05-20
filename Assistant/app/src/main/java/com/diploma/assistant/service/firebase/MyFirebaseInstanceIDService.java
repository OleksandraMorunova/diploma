package com.diploma.assistant.service.firebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.diploma.assistant.R;
import com.diploma.assistant.view_model.UserViewModel;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);

        sendRegistrationToServer(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("OnMessageReceived", "Token from: " + message.getFrom());

        if ("Нове повідомлення з чату".equals(Objects.requireNonNull(message.getNotification()).getTitle())) {
            Intent intent = new Intent("YOUR_CUSTOM_ACTION");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    @Override
    public void onMessageSent(@NonNull String msgId) {
        super.onMessageSent(msgId);
        Log.d("MyFirebaseMessaging", "Message sent. Message ID: " + msgId);
    }

    private void sendRegistrationToServer(String token){
        SharedPreferences settings = getSharedPreferences("com.assistant.emmotechie.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        settings.edit().putString("firebase_token", token).apply();
    }
}