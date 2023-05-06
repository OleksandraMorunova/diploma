package com.diploma.assistant.view.ui.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.TasksListActivityFinallyBinding;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.adapter.RecycleViewTasks;
import com.diploma.assistant.view_model.FilesViewModel;
import com.diploma.assistant.view_model.UserViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Base64;
import java.util.List;

public class TasksListActivity extends AppCompatActivity {
    private TasksListActivityFinallyBinding binding;
    private RecyclerView recycleView;
    private List<TaskDto> listTask;
    private RecycleViewTasks adapter;

    String id, name, count, token, imageId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TasksListActivityFinallyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recycleView = findViewById(R.id.recycle_view_tasks_list);

        AuthenticatorService accounts = new AuthenticatorService(getApplicationContext());
        token = accounts.getElementFromSet("Bearer", "jwt_token","com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        id = getIntent().getStringExtra("id");
        imageId = getIntent().getStringExtra("image_id");
        name = getIntent().getStringExtra("name");
        count = getIntent().getStringExtra("count");
        Bitmap icon = (Bitmap) getIntent().getParcelableExtra("icon");

        TextView nameView = findViewById(R.id.name_user_tasks_list);
        nameView.setText(name);
        TextView countView = findViewById(R.id.count_tasks_list);
        String newCount = "Кількість завдань: " + count;
        countView.setText(newCount);

        ShapeableImageView imageContainer = findViewById(R.id.image_view_tasks_list);
        if(icon != null){
                imageContainer.setImageBitmap(icon);
        } else imageContainer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_user, getTheme()));

        binding.searchViewTasksList.setImeOptions(EditorInfo.IME_ACTION_DONE);

        UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getDetailsUser(id, token).observe(this, u -> {
            if(u != null){
              listTask = u.getTaskDto();
              recycleView.setHasFixedSize(true);
              recycleView.setLayoutManager(new LinearLayoutManager(this));
              adapter = new RecycleViewTasks(this,  listTask);
              recycleView.setAdapter(adapter);

              binding.searchViewTasksList.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                  @Override
                  public boolean onQueryTextSubmit(String query) { return false; }

                  @Override
                  public boolean onQueryTextChange(String newText) {
                      adapter.getFilter().filter(newText);
                      return false;
                  }
              });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

