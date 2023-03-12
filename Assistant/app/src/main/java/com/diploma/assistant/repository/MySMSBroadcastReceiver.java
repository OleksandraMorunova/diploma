package com.diploma.assistant.repository;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class MySMSBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    System.out.println(message);

                    Log.i("SmsReceiver", "onReceive: "+ message);
                    break;

                case CommonStatusCodes.TIMEOUT:
                    Log.e("SmsReceiver", "onReceive: failure");
                    break;

                case CommonStatusCodes.API_NOT_CONNECTED:
                    Log.e("SmsReceiver", "API NOT CONNECTED");
                    break;

                case CommonStatusCodes.NETWORK_ERROR:
                    Log.e("SmsReceiver", "NETWORK ERROR");
                    break;

                case CommonStatusCodes.ERROR:
                    Log.e("SmsReceiver", "SOME THING WENT WRONG");
                    break;
            }
        }
    }
}
