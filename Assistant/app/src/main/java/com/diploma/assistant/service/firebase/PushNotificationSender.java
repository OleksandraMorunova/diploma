package com.diploma.assistant.service.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationSender {
    public void sendNotification(String token, String title, String body) {
        RemoteMessage message = new RemoteMessage.Builder(token)
                .setMessageId(Integer.toString(getRandomMessageId()))
                .addData("title", title)
                .addData("body", body)
                .build();
        FirebaseMessaging.getInstance().send(message);
    }

    private int getRandomMessageId() {
        return (int) (Math.random() * 1000);
    }
}
