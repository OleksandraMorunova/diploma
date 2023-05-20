package com.diploma.assistant.view.ui.tasks.user;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.ActivityUserAddTaskBinding;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.entity.resource_service.ResponseTask;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.service.firebase.PushNotificationSender;
import com.diploma.assistant.service.work_on_files.DownloadFiles;
import com.diploma.assistant.service.work_on_files.DownloadNotificationWorker;
import com.diploma.assistant.service.work_on_files.GetDataFromUri;
import com.diploma.assistant.view.adapter.RecycleViewAddedDocument;
import com.diploma.assistant.view_model.TaskResponseViewModel;
import com.diploma.assistant.view_model.UserViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;

public class AddTaskFragment extends AppCompatActivity {
    private RecyclerView recycleView;
    private final List<MultipartBody.Part> files = new ArrayList<>();
    private List<Uri> mUriList;
    private ActivityUserAddTaskBinding binding;

    private final List<User> userList = new ArrayList<>();

    private static final String LIST_IMAGES = "listimages";
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 1;
    public static final String THREAD_NAME_DOWNLOAD_FILES = "DownloadThread";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserAddTaskBinding.inflate(getLayoutInflater());
        mUriList = savedInstanceState != null ? savedInstanceState.getParcelableArrayList(LIST_IMAGES) : new ArrayList<>();
        setContentView(binding.getRoot());

        recycleView = findViewById(R.id.card_view_add_task_list_fr);
        putToRecycleView(mUriList);

        AuthenticatorService accounts = new AuthenticatorService(this);
        String token = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        DownloadFiles downloadFiles = new DownloadFiles(this);
        String id = getIntent().getStringExtra("id_task");
        MaterialToolbar toolbar = binding.toolbarTilFr;

        toolbar.findViewById(R.id.added_file_to_task).setOnClickListener(v -> {
            Thread downloadThread = new Thread(() -> {
                Log.i("Поточний потік: " , Thread.currentThread().getName());
                downloadFiles.openFile(REQUEST_CODE_OPEN_DOCUMENT);
            });
            downloadThread.setName(THREAD_NAME_DOWNLOAD_FILES);
            downloadThread.start();
        });

        String userId = accounts.getStringFromSharedPreferences("id_user", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");

        toolbar.findViewById(R.id.send_task).setOnClickListener(v -> {
            String text = Objects.requireNonNull(binding.descriptionFr.getText()).toString();
            ResponseTask responseTask = new ResponseTask();
            responseTask.setText(text);
            responseTask.setTaskId(id);
            responseTask.setUserId(userId);
            TaskResponseViewModel responseViewModel = new ViewModelProvider(this).get(TaskResponseViewModel.class);

            responseViewModel.createTaskResponse(token, responseTask ,files).observe(this, o -> {
                if(o.equals(false)){
                    Toast.makeText(this, "Щось пішло не так, спробуйте будь-ласка ще раз", Toast.LENGTH_SHORT).show();
                } else {
                    UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
                    userViewModel.getUsers(token).observe(this, users -> {
                        if (users != null) {
                            userList.addAll(users.getUserList());
                            userList.removeIf(r -> !r.getRoles().contains("ADMIN"));

                            for(User u: userList){
                                if(! u.getUserTokenFirebase().isEmpty()){
                                    if (Build.VERSION.SDK_INT >= 33) {
                                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                                            PushNotificationSender sender = new PushNotificationSender();
                                            sender.sendNotification( u.getUserTokenFirebase(), "Нове повідомлення з чату", text);
                                        }
                                    } else {
                                        PushNotificationSender sender = new PushNotificationSender();
                                        sender.sendNotification( u.getUserTokenFirebase(), "Нове повідомлення з чату", text);
                                    }
                                }
                            }

                        }
                    });
                }
            });
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
}