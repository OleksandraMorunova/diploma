package com.diploma.assistant.service.work_on_files;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import android.provider.DocumentsContract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GetDataFromUri {
    private Activity activity;

    public GetDataFromUri(Activity activity) {
        this.activity = activity;
    }

    public GetDataFromUri() {
    }

    public String getFileNameFromUri(Context context, Uri uri) {
        String fileName = null;
        Cursor cursor = null;
        try {
            String[] projection = {DocumentsContract.Document.COLUMN_DISPLAY_NAME};
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DISPLAY_NAME);
                fileName = cursor.getString(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return fileName;
    }

    public MultipartBody.Part createMultipartBody(Uri uri) throws IOException {
      if(uri != null){
          System.out.println(activity);
          InputStream inputStream = activity.getContentResolver().openInputStream(uri);
          String mimeType = activity.getContentResolver().getType(uri);
          String[] split = mimeType.split("/");
          String extension = split[1];
          File file = File.createTempFile("temp", "." + extension, activity.getCacheDir());

          FileOutputStream out = new FileOutputStream(file);
          int bytesRead;
          byte[] buffer = new byte[1024];
          while ((bytesRead = inputStream.read(buffer)) != -1) {
              out.write(buffer, 0, bytesRead);
          }
          inputStream.close();
          out.flush();
          out.close();

          RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
          return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
      }
      return null;
    }

}
