package com.diploma.assistant.service.work_on_files;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class DownloadFiles {
    private Activity activity;

    private static final int CREATE_FILE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    public DownloadFiles(Activity activity) {
        this.activity = activity;
    }

    //Analog "save as"
    public void createFile(Uri pickerInitialUri, String typeOfFile, String value) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(typeOfFile);
        intent.putExtra(Intent.EXTRA_TITLE, value);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
        activity.startActivityForResult(intent, CREATE_FILE);
    }

    //Opens the system's file picker app
    public void openFile(int REQUEST_CODE_OPEN_DOCUMENT) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("*/*");
        activity.startActivityForResult(Intent.createChooser(intent, "Open file using"), REQUEST_CODE_OPEN_DOCUMENT);
    }

    public void openGallery(int REQUEST_CODE_OPEN_DOCUMENT){
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            activity.startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE_OPEN_DOCUMENT);
        }
    }

    public void openConcreteFile(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, activity.getContentResolver().getType(uri));
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            activity.startActivity(Intent.createChooser(intent, "Open file using"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity.getApplicationContext(), "No app found to open this file", Toast.LENGTH_SHORT).show();
        }
    }
}
