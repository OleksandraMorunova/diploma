package com.diploma.assistant.service.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);

        SharedPreferences settings = getSharedPreferences("com.assistant.emmotechie.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        settings.edit().putString("firebase_token", s).apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Log.d("OnMessageReceived", "Token from: " + message.getFrom());

        if (message.getData().size() > 0) {
            Log.d("OnMessageReceived", "Message data payload: " + message.getData());

        }

        if (message.getNotification() != null) {
            Log.d("OnMessageReceived", "Message Notification Body: "  + message.getNotification().getBody());
        }
    }
}