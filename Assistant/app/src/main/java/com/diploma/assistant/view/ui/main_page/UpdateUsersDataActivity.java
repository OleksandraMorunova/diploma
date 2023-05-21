package com.diploma.assistant.view.ui.main_page;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.UpdateUsersDataBinding;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.service.work_on_files.DownloadFiles;
import com.diploma.assistant.service.work_on_files.DownloadNotificationWorker;
import com.diploma.assistant.service.work_on_files.GetDataFromUri;
import com.diploma.assistant.view_model.UserViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MultipartBody;

public class UpdateUsersDataActivity extends AppCompatActivity {
    private UpdateUsersDataBinding binding;
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 2;
    public static final String THREAD_NAME_DOWNLOAD_FILES = "DownloadThread";

    private  MultipartBody.Part part;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UpdateUsersDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AuthenticatorService accounts = new AuthenticatorService(this);
        String token = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        String userId = accounts.getStringFromSharedPreferences("id_user", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        MaterialToolbar toolbar = binding.toolbarUpdateUsersData;

        String name = getIntent().getStringExtra("name");
        String phoneNumber = getIntent().getStringExtra("phone_number");
        byte[] byteArray = getIntent().getByteArrayExtra("icon");

        binding.updateNameTiet.setText(name);
        binding.updatePhoneNumberTiet.setText(phoneNumber);

        if(byteArray != null){
            Bitmap icon = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            binding.updateImage.setImageBitmap(icon);
        } else binding.updateImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_user, getTheme()));

        binding.imageUpdateChange.setOnClickListener(c -> {
            DownloadFiles downloadFiles = new DownloadFiles(this);
            Thread downloadThread = new Thread(() -> {
                Log.i("Поточний потік: ", Thread.currentThread().getName());
                downloadFiles.openGallery(REQUEST_CODE_OPEN_DOCUMENT);
            });
            downloadThread.setName(THREAD_NAME_DOWNLOAD_FILES);
            downloadThread.start();
        });

        toolbar.findViewById(R.id.send_update_data).setOnClickListener(v -> {
            User user = new User();
            user.setName(Objects.requireNonNull(binding.updateNameTiet.getText()).toString());
            user.setPhone(Objects.requireNonNull(binding.updatePhoneNumberTiet.getText()).toString());

            UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            final int[] i = {0};
            userViewModel.updateUserDetails(userId, user, part).observe(this, o -> {
               while (i[0] < 1){
                   if(o != null) Toast.makeText(this, "Дані успішно оновленні", Toast.LENGTH_SHORT).show();
                   else Toast.makeText(this, "Щось пішло не так, спробуйте будь-ласка ще раз", Toast.LENGTH_SHORT).show();
                   i[0]++;
               }
            });
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        GetDataFromUri parseUri = new GetDataFromUri(this);
        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT && resultCode == RESULT_OK) {
            Uri uri = resultData.getData();
            runOnUiThread(() -> {
                if(uri != null){
                    Data inputData = new Data.Builder()
                            .putString("file_uri", uri.toString())
                            .build();
                    OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(DownloadNotificationWorker.class).setInputData(inputData).build();
                    WorkManager.getInstance(this).enqueue(request);
                    binding.updateImage.setImageURI(uri);
                    try {
                        part = parseUri.createMultipartBody(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}