package com.diploma.assistant.view.ui.tasks.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.ActivityAddTaskBinding;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.service.firebase.PushNotificationSender;
import com.diploma.assistant.service.work_on_files.DownloadFiles;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.service.work_on_files.DownloadNotificationWorker;
import com.diploma.assistant.service.work_on_files.GetDataFromUri;
import com.diploma.assistant.view.adapter.RecycleViewAddedDocument;
import com.diploma.assistant.view_model.TasksViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import okhttp3.MultipartBody;

public class AddTaskActivity extends AppCompatActivity {
    private RecyclerView recycleView;
    private final List<MultipartBody.Part> files = new ArrayList<>();
    private List<Uri> mUriList;
    private String token, firebaseToken;
    private ActivityAddTaskBinding binding;

    private static final String LIST_IMAGES = "listimages";
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 1;
    public static final String THREAD_NAME_DOWNLOAD_FILES = "DownloadThread";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        mUriList = savedInstanceState != null ? savedInstanceState.getParcelableArrayList(LIST_IMAGES) : new ArrayList<>();
        setContentView(binding.getRoot());

        recycleView = findViewById(R.id.card_view_add_task_list);
        putToRecycleView(mUriList);

        AuthenticatorService accounts = new AuthenticatorService(this);
        token = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        firebaseToken = getIntent().getStringExtra("firebase_token");
        DownloadFiles downloadFiles = new DownloadFiles(this);
        String idUser = getIntent().getStringExtra("id_user");
        MaterialToolbar toolbar = binding.toolbarTil;

        toolbar.findViewById(R.id.added_file_to_task).setOnClickListener(v -> {
            Thread downloadThread = new Thread(() -> {
                Log.i("Поточний потік: " , Thread.currentThread().getName());
                downloadFiles.openFile(REQUEST_CODE_OPEN_DOCUMENT);
            });
            downloadThread.setName(THREAD_NAME_DOWNLOAD_FILES);
            downloadThread.start();
        });

            binding.title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (Objects.requireNonNull(binding.title.getText()).length() != 0) {
                        binding.titleTil.setHelperTextEnabled(false);
                        binding.titleTil.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(AddTaskActivity.this, R.color.primary)));
                        binding.titleTil.setBoxStrokeColor(ContextCompat.getColor(AddTaskActivity.this, R.color.box_color_selector));
                    } else {
                        setToTitleFieldsAboutEmpty();
                    }
                }
            });

        toolbar.findViewById(R.id.send_task).setOnClickListener(v -> {
                if (Objects.requireNonNull(binding.title.getText()).length() != 0) {
                    TaskDto taskDto = new TaskDto();
                    if (idUser != null) {
                        taskDto.setUserId(idUser);
                    }
                    taskDto.setTitle(binding.title.getText().toString());
                    if (binding.description.getText() != null)
                        taskDto.setDescription(Objects.requireNonNull(binding.description.getText()).toString());
                    Toast.makeText(this, "Завдання відправлено", Toast.LENGTH_SHORT).show();
                    uploadFilesToDataBase(taskDto, files);
                } else {
                    setToTitleFieldsAboutEmpty();
                    Toast.makeText(this, "Ви маєте пустий заголовк", Toast.LENGTH_SHORT).show();
                }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(LIST_IMAGES, new ArrayList<>(mUriList));
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.added_file_to_task) {
            return true;
        } else if(item.getItemId() == R.id.send_task){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        GetDataFromUri get = new GetDataFromUri(this);
        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT && resultCode == RESULT_OK) {
            ClipData clipData = resultData.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    runOnUiThread(() -> {
                        try {
                            Data inputData = new Data.Builder()
                                    .putString("file_uri", uri.toString())
                                    .build();
                            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(DownloadNotificationWorker.class).setInputData(inputData).build();
                            WorkManager.getInstance(this).enqueue(request);
                            files.add(get.createMultipartBody(uri));
                            mUriList.add(uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } else {
                Uri uri = resultData.getData();
                runOnUiThread(() -> {
                    if(uri != null){
                        try {
                            Data inputData = new Data.Builder()
                                    .putString("file_uri", uri.toString())
                                    .build();
                            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(DownloadNotificationWorker.class).setInputData(inputData).build();
                            WorkManager.getInstance(this).enqueue(request);
                            files.add(get.createMultipartBody(uri));
                            mUriList.add(uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
           putToRecycleView(mUriList);
        }
    }

    private void putToRecycleView(List<Uri> mUriList){
        RecycleViewAddedDocument mAdapter = new RecycleViewAddedDocument(this, mUriList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(mAdapter);
    }

    private void uploadFilesToDataBase(TaskDto taskDto, List<MultipartBody.Part> files){
        TasksViewModel viewModel = new ViewModelProvider(this).get(TasksViewModel.class);
           viewModel.createTask(token, taskDto, files).observe(this, o -> {
               if(o.equals(true)){
                   if (Build.VERSION.SDK_INT >= 33) {
                       if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                           PushNotificationSender sender = new PushNotificationSender();
                           sender.sendNotification(firebaseToken, "Вам надіслане нове завдання", taskDto.getTitle());
                       }
                   } else {
                       PushNotificationSender sender = new PushNotificationSender();
                       sender.sendNotification(firebaseToken, "Вам надіслане нове завдання", taskDto.getTitle());
                   }
                   Toast.makeText(this, "Файли були успішно завантаженні", Toast.LENGTH_SHORT).show();
               } else Toast.makeText(this, "Щось е так з даними, перевірте коректність файлів", Toast.LENGTH_SHORT).show();
           });
    }

    private void setToTitleFieldsAboutEmpty(){
        binding.titleTil.setHelperTextEnabled(true);
        binding.titleTil.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(AddTaskActivity.this, R.color.error)));
        binding.titleTil.setBoxStrokeColor(ContextCompat.getColor(AddTaskActivity.this, R.color.error));
        binding.titleTil.setHelperText("Пусте поле");
    }
}