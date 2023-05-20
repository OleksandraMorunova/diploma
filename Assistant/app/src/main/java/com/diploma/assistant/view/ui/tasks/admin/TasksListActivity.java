package com.diploma.assistant.view.ui.tasks.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.TasksListActivityFinallyBinding;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.ui.tasks.GetTaskListForCertainUser;
import com.diploma.assistant.view_model.FilesViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Base64;

public class TasksListActivity extends AppCompatActivity {
    private TasksListActivityFinallyBinding binding;
    private RecyclerView recycleView;

    String id, name, count, token, firebaseToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TasksListActivityFinallyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recycleView = findViewById(R.id.recycle_view_tasks_list);

        AuthenticatorService accounts = new AuthenticatorService(this);
        token = accounts.getElementFromSet("Bearer", "jwt_token","com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        count = getIntent().getStringExtra("count");
        firebaseToken = getIntent().getStringExtra("firebase_token");
        String icon =  getIntent().getStringExtra("icon");

        TextView nameView = findViewById(R.id.name_user_tasks_list);
        nameView.setText(name);
        TextView countView = findViewById(R.id.count_tasks_list);
        String newCount = "Кількість завдань: " + count;
        countView.setText(newCount);


        SwipeRefreshLayout swipe = findViewById(R.id.swipe_list_task);
        swipe.setOnRefreshListener(() -> {
            GetTaskListForCertainUser get = new GetTaskListForCertainUser(binding, this, this, this);
            get.getTaskForUser(recycleView, id, token, name);
            swipe.setRefreshing(false);
        });

        ShapeableImageView imageContainer = findViewById(R.id.image_view_tasks_list);
        if(icon != null){
            FilesViewModel viewModel = new ViewModelProvider(this).get(FilesViewModel.class);
            viewModel.getFiles(token, icon).observe(this, f -> {
                byte[] bytes = Base64.getDecoder().decode(f.getFile());
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageContainer.setImageBitmap(bitmap);
            });
        } else imageContainer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_user, getTheme()));

        binding.searchViewTasksList.setImeOptions(EditorInfo.IME_ACTION_DONE);

        binding.fabListTasks.setOnClickListener(v ->
                this.startActivity(new Intent(this, AddTaskActivity.class)
                        .putExtra("id_user", id)
                        .putExtra("firebase_token", firebaseToken))
        );
        GetTaskListForCertainUser get = new GetTaskListForCertainUser(binding, this, this, this);
        get.getTaskForUser(recycleView, id, token, name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}