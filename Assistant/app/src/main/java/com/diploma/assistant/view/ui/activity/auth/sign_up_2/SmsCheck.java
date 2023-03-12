package com.diploma.assistant.view.ui.activity.auth.sign_up_2;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.diploma.assistant.repository.MySMSBroadcastReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;

public class SmsCheck {
    private final Context context;

    public SmsCheck(Context context) {
        this.context = context;
    }

    public void getSms(){
        try {
            IntentFilter intentFilter = new IntentFilter("com.google.android.gms.auth.api.phone.SMS_RETRIEVED");
            MySMSBroadcastReceiver otpReceiver = new MySMSBroadcastReceiver();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            context.registerReceiver(otpReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(context);
            Task<Void> task = client.startSmsRetriever();

            task.addOnSuccessListener(success -> Log.i("TAG", "SMS Retriever starts"));
            task.addOnFailureListener(failure -> Log.e("TAG", "Cannot Start SMS Retriever"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
