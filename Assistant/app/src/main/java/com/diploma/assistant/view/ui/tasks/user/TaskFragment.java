package com.diploma.assistant.view.ui.tasks.user;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.LookTaskCordinatorBinding;
import com.diploma.assistant.databinding.LookUserTaskCordinatorBinding;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfCommentsCertain;
import com.diploma.assistant.model.entity.registration_service.LoadFile;
import com.diploma.assistant.model.entity.resource_service.CommentsDto;
import com.diploma.assistant.model.entity.resource_service.ResponseTask;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.model.enumaration.CheckTask;
import com.diploma.assistant.model.enumaration.TypeUserEnum;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.adapter.RecycleViewCommentsCertainContext;
import com.diploma.assistant.view.adapter.RecycleViewLookDocument;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_2.CheckStringLine;
import com.diploma.assistant.view.ui.tasks.admin.AddTaskActivity;
import com.diploma.assistant.view_model.CommentsViewModel;
import com.diploma.assistant.view_model.FilesViewModel;
import com.diploma.assistant.view_model.TaskResponseViewModel;
import com.diploma.assistant.view_model.TasksViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class TaskFragment extends AppCompatActivity {
    private LookUserTaskCordinatorBinding binding;

    private final List<String> mDocList = new ArrayList<>();
    private ResponseTask responseTask = new ResponseTask();
    private final List<LoadFile> mLoadFiles = new ArrayList<>();
    private final List<LoadFile> mRloadFiles = new ArrayList<>();
    private final List<ItemsForListOfCommentsCertain> it = new ArrayList<>();
    private final List<String> user = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecycleViewLookDocument mAdapter, mRAdapter;
    private RecycleViewCommentsCertainContext commentAdapter;
    private String id, userId, name, token;
    private LinearLayout l;

    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LookUserTaskCordinatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        l = findViewById(R.id.bottom_sheet_look_task);
        TextInputLayout inputLayout = l.findViewById(R.id.comment_text_input_layout_look_task);
        TextInputEditText input = l.findViewById(R.id.comment_text_input_edit_text_look_task);
        recyclerView = l.findViewById(R.id.card_view_look_task);

        AuthenticatorService accounts = new AuthenticatorService(this);
        token = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        id = getIntent().getStringExtra("id");
        userId = accounts.getStringFromSharedPreferences("id_user", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        name = getIntent().getStringExtra("name");

           toolbar = findViewById(R.id.toolbar_look_task_fr);
           toolbar.findViewById(R.id.delete_task).setOnClickListener(v -> {
             TaskResponseViewModel responseViewModel = new ViewModelProvider(this).get(TaskResponseViewModel.class);
             responseViewModel.deleteTaskResponse(token, id).observe(this, d -> {
                 if(d.equals(false)){
                     Toast.makeText(this, "Щось пішло не так, спробуйте будь-ласка ще раз", Toast.LENGTH_SHORT).show();
                 } else {
                     Toast.makeText(this, "Повідомлення успішно видалене", Toast.LENGTH_SHORT).show();
                     TextView description = l.findViewById(R.id.text_tasks_look);
                     description.setText(null);
                 }
             });
           });

           toolbar.findViewById(R.id.add_task).setOnClickListener(v -> {
               if(responseTask.getId() == null){
                   this.startActivity(new Intent(this, AddTaskFragment.class)
                           .putExtra("id_task", id));
               } else {
                   Toast.makeText(this, "Ви вже додали завдання, видаліть та спробуйте ще раз", Toast.LENGTH_SHORT).show();
               }
           });

        inputLayout.setEndIconOnClickListener(v -> {
            CommentsViewModel viewModel = new ViewModelProvider(this).get(CommentsViewModel.class);
            CommentsDto dto = new CommentsDto();
            dto.setUser_comment_id(id);
            dto.setComment(Objects.requireNonNull(input.getText()).toString());
            viewModel.postComment(token, id, dto).observe(this, p -> {
                it.add(new ItemsForListOfCommentsCertain(CheckStringLine.parserData(p.getAddedData()), input.getText().toString()));
                user.add(userId);
                adapterComment(it, user, userId);
                input.setText(null);
            });
        });

        uploadResponseToTask(l);

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(l);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight(115);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        bottomSheetBehavior.setMaxHeight(displayMetrics.heightPixels - 200);
        bottomSheetBehavior.setHideable(false);

        findViewById(R.id.fab_look_fr).setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) { }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                findViewById(R.id.fab_look_fr).animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });
        callToTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        clearCache(getCacheDir());
    }

    private void clearCache(File cacheDir) {
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File child : files) {
                boolean isDeleted = child.delete();
                if (!isDeleted) {
                    Log.e("ClearCache", "Failed to delete file: " + child.getAbsolutePath());
                } else
                    Log.i("Delete files in content://com.diploma.assistant.fileprovider/cache/", "Successfully");
            }
        } else Log.e("Delete files in content://com.diploma.assistant.fileprovider/cache/", "No successfully, no element");
    }

    private void adapterComment(List<ItemsForListOfCommentsCertain> it, List<String> user, String userId){
        RecyclerView navRecyclerView = findViewById(R.id.comment_look_task);
        commentAdapter = new RecycleViewCommentsCertainContext(this, it, user, userId);
        navRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        navRecyclerView.setAdapter(commentAdapter);
    }

    private void initRecyclerView(RecyclerView mRecyclerView) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int cardWidth = getResources().getDimensionPixelSize(R.dimen.dimen_150dp);
        int spacing = getResources().getDimensionPixelSize(R.dimen.dimen_10dp);
        int numColumns = (screenWidth - spacing) / (cardWidth + spacing);
        GridLayoutManager layoutManager = new GridLayoutManager(this, GridLayoutManager.DEFAULT_SPAN_COUNT);
        layoutManager.setSpanCount(numColumns);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void callToTask(){
        TasksViewModel viewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        viewModel.getTaskByIDTask(token, id).observe(this, t -> {
            if (t != null) {
                mDocList.clear();
                if (t.getFiles() != null) mDocList.addAll(t.getFiles());
                for(int i = 0; i < mDocList.size(); i++){
                    FilesViewModel vviewModel = new ViewModelProvider(this).get(FilesViewModel.class);
                    final int finalI = i;
                    vviewModel.getFiles(token, mDocList.get(i)).observe(this, data -> {
                        if(finalI == mDocList.size() - 1) {
                            mLoadFiles.add(data);
                            if(mLoadFiles.size() == mDocList.size()) mAdapter.notifyDataSetChanged();
                        }
                    });
                }

                TextView userTasks = findViewById(R.id.user_tasks);
                userTasks.setText(name);
                TextView titleTasks = findViewById(R.id.title_tasks);
                titleTasks.setText(t.getTitle());
                TextView datatimeTasks = findViewById(R.id.datatime_tasks);
                datatimeTasks.setText(CheckStringLine.parserData(t.getAddedData()));
                TextView descriptionTasks = findViewById(R.id.description_tasks);
                descriptionTasks.setText(t.getDescription() != null ? t.getDescription() : null);
                TextView auditTasks = toolbar.findViewById(R.id.text_audit_task_fr);

                if(t.getAudit().equals(CheckTask.RETURNED.getCheckLine())){
                    auditTasks.setText("Повернуто");
                    auditTasks.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.error)));
                } else if (t.getAudit().equals(CheckTask.REVIEWED.getCheckLine())){
                    auditTasks.setText("Переглянуто");
                    auditTasks.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
                } else auditTasks.setText("Не переглянуто");

                if(t.getComments() != null){
                    for(CommentsDto dto : t.getComments()){
                        user.add(dto.getUser_comment_id());
                        it.add(new ItemsForListOfCommentsCertain(CheckStringLine.parserData(dto.getComment_added_data()), dto.getComment()));
                    }
                }
                commentAdapter.notifyDataSetChanged();
            } else Toast.makeText(this, "Щось пішло не так, спробуйте ще раз", Toast.LENGTH_SHORT).show();
        });

        RecyclerView mRecyclerView = findViewById(R.id.card_view_add_task);
        mAdapter = new RecycleViewLookDocument(this, mLoadFiles);
        mRecyclerView.setAdapter(mAdapter);
        initRecyclerView(mRecyclerView);
        adapterComment(it, user, userId);
    }

    private void uploadResponseToTask(LinearLayout l){
        TaskResponseViewModel viewModel = new ViewModelProvider(this).get(TaskResponseViewModel.class);
        viewModel.getAllTaskResponseByIdUser(token, id).observe(this, i -> {
            if(i != null){
                responseTask = i;
                TextView description = l.findViewById(R.id.text_tasks_look);
                description.setText(i.getText() == null ? "Немає опису завдання" : i.getText());
                List<String> doc = i.getFiles();
                for(int j = 0; j < doc.size(); j++){
                    int finalI = j;
                    FilesViewModel vviewModel = new ViewModelProvider(this).get(FilesViewModel.class);
                    vviewModel.getFiles(token, doc.get(j)).observe(this, data -> {
                        if(finalI == doc.size() - 1) {
                            mRloadFiles.add(data);
                            if(mLoadFiles.size() == mDocList.size()){
                                mRAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                }
            }
        });

        mRAdapter = new RecycleViewLookDocument(this, mRloadFiles);
        recyclerView.setAdapter(mRAdapter);
        initRecyclerView(recyclerView);
    }
}