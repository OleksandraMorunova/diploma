package com.diploma.assistant.view.ui.tasks.admin;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.diploma.assistant.model.entity.adapter.ItemsForListOfCommentsCertain;
import com.diploma.assistant.model.entity.registration_service.LoadFile;
import com.diploma.assistant.model.entity.resource_service.CommentsDto;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.model.enumaration.CheckTask;
import com.diploma.assistant.model.enumaration.TypeUserEnum;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.adapter.RecycleViewCommentsCertainContext;
import com.diploma.assistant.view.adapter.RecycleViewLookDocument;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_2.CheckStringLine;
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

public class TaskActivity extends AppCompatActivity {
    private LookTaskCordinatorBinding binding;

    private final List<String> mDocList = new ArrayList<>();
    private final List<LoadFile> mLoadFiles = new ArrayList<>();
    private final List<LoadFile> mRloadFiles = new ArrayList<>();
    private final List<ItemsForListOfCommentsCertain> it = new ArrayList<>();
    private final List<String> user = new ArrayList<>();

    private RecycleViewLookDocument mAdapter, mRAdapter;
    private RecycleViewCommentsCertainContext commentAdapter;
    private String id, userId, name, token;

    private MaterialToolbar toolbar;

    private boolean checkExistResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LookTaskCordinatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AuthenticatorService accounts = new AuthenticatorService(this);
        token = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        id = getIntent().getStringExtra("id");
        userId = accounts.getStringFromSharedPreferences("id_user", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        name = getIntent().getStringExtra("name");

        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode("uu74l8S6ewO/Nmrh3waPdCfyF7UFTUtFoI44Z5c75X0="));
        Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token.replace("Bearer ", "")).getBody();
        List<String> roles = (List<String>) claims.get("role");

        if(roles.get(0).equals(TypeUserEnum.ADMIN.getTypeUserName())){
           toolbar = findViewById(R.id.toolbar_look_task);
           toolbar.findViewById(R.id.check_task).setOnClickListener(v -> {
               PopupMenu popupMenu = new PopupMenu(this, toolbar.findViewById(R.id.check_task));
               popupMenu.getMenuInflater().inflate(R.menu.look_task_menu, popupMenu.getMenu());
               popupMenu.setOnMenuItemClickListener(menuItem -> {
                   if(menuItem.getItemId() == R.id.returned) {
                       updateTask(CheckTask.RETURNED.getCheckLine());
                       return true;
                   }
                   if (menuItem.getItemId() == R.id.reviewed){
                       updateTask(CheckTask.REVIEWED.getCheckLine());
                       return true;
                   }
                   return false;
               });
               popupMenu.show();
           });
       } else {
           toolbar = findViewById(R.id.toolbar_look_task);
           toolbar.findViewById(R.id.check_task).setOnClickListener(c -> {
               this.startActivity(new Intent(this, AddTaskActivity.class)
                       .putExtra("id_task", id)
                       .putExtra("check_exist", checkExistResponse));
           });
       }

        LinearLayout l = findViewById(R.id.bottom_sheet_look_task);

        TextInputLayout inputLayout = l.findViewById(R.id.comment_text_input_layout_look_task);
        TextInputEditText input = l.findViewById(R.id.comment_text_input_edit_text_look_task);
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

        TaskResponseViewModel viewModel = new ViewModelProvider(this).get(TaskResponseViewModel.class);
        viewModel.getAllTaskResponseByIdUser(token, id).observe(this, i -> {
            if(i != null){
                checkExistResponse = true;
                TextView description = l.findViewById(R.id.text_tasks_look);
                description.setText(i.getText());
                List<String> doc = i.getFiles();
                for(int j = 0; j < doc.size(); j++){
                    int finalI = j;
                    FilesViewModel vviewModel = new ViewModelProvider(this).get(FilesViewModel.class);
                    vviewModel.getFiles(token, doc.get(j)).observe(this, data -> {
                        if(finalI == doc.size() - 1) {
                            mRloadFiles.add(data);
                            if(mLoadFiles.size() == doc.size()) {
                                mRAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            } else checkExistResponse = false;
        });

        RecyclerView recyclerView = l.findViewById(R.id.card_view_look_task);
        mRAdapter = new RecycleViewLookDocument(this, mRloadFiles);
        recyclerView.setAdapter(mRAdapter);
        initRecyclerView(recyclerView);

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(l);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight(115);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        bottomSheetBehavior.setMaxHeight(displayMetrics.heightPixels - 200);
        bottomSheetBehavior.setHideable(false);

        findViewById(R.id.fab_look).setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) { }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                findViewById(R.id.fab_look).animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });

        callToTask();
    }

    private void updateTask(String audit){
        TasksViewModel tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        TaskDto taskDto = new TaskDto();
        taskDto.setAudit(audit);
        tasksViewModel.updateTask(token, id, taskDto,null).observe(this, t -> {
            if(t != null){
                Toast.makeText(this, "Дані успішно зміненно", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, "Щось пішло не так, спробуйте ще раз", Toast.LENGTH_SHORT).show();
        });
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
                TextView datatimeTasks = findViewById(R.id.datatime_tasks);
                datatimeTasks.setText(CheckStringLine.parserData(t.getAddedData()));
                TextView titleTasks = findViewById(R.id.title_tasks);
                titleTasks.setText(t.getTitle());
                TextView descriptionTasks = findViewById(R.id.description_tasks);
                descriptionTasks.setText(t.getDescription() != null ? t.getDescription() : null);
                TextView auditTasks = toolbar.findViewById(R.id.text_audit_task);

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
}