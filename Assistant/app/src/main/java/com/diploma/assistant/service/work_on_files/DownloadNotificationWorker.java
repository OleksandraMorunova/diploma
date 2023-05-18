package com.diploma.assistant.service.work_on_files;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.diploma.assistant.R;

import java.io.IOException;
import java.io.InputStream;


public class DownloadNotificationWorker extends Worker {
    private final Context mContext;
    private  Uri mUri;


    public DownloadNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        mUri = Uri.parse(getInputData().getString("file_uri"));
        downloadFile();
        return Result.success();
    }


    private void downloadFile() {
        try {
            ContentResolver contentResolver = mContext.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(mUri);
            long fileSize = contentResolver.openAssetFileDescriptor(mUri, "r").getLength();
            byte[] buffer = new byte[1024];
            int bytesRead;
            long bytesDownloaded = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bytesDownloaded += bytesRead;
                int progress = (int) (bytesDownloaded * 100 / fileSize);
                sendNotification(progress);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(int progress){
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel("CHANNEL_ID", "My Notifications", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, "CHANNEL_ID");
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Завантаження файлів")
                .setSound(null)
                .setProgress(100, progress, false);
        notificationManager.notify(1, notificationBuilder.build());
    }
}
