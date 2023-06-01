package com.diploma.assistant.view.ui.tasks.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.TasksListActivityBinding;
import com.diploma.assistant.databinding.TasksListActivityFinallyBinding;
import com.diploma.assistant.model.entity.resource_service.ArticleWithId;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.model.enumaration.ErrorEnum;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.ui.tasks.admin.AddTaskActivity;
import com.diploma.assistant.view.ui.tasks.GetTaskListForCertainUser;
import com.diploma.assistant.view_model.FilesViewModel;
import com.diploma.assistant.view_model.UserViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TasksListFragment extends Fragment {
    private TasksListActivityFinallyBinding binding;

    String id, token;
    byte[] bytes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = TasksListActivityFinallyBinding.inflate(inflater, container, false);
        AuthenticatorService accounts = new AuthenticatorService(getActivity());
        token = accounts.getElementFromSet("Bearer", "jwt_token","com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        id = accounts.getStringFromSharedPreferences("id_user", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");

        binding.fabListTasks.setVisibility(View.INVISIBLE);
        binding.searchViewTasksList.setImeOptions(EditorInfo.IME_ACTION_DONE);
        RecyclerView recycleView = binding.ms.recycleViewTasksList;

        SwipeRefreshLayout swipe = binding.ms.swipeListTask;
        swipe.setOnRefreshListener(() -> {
            GetTaskListForCertainUser get = new GetTaskListForCertainUser(binding, this, this, getActivity());
            get.getTaskForUser(recycleView, id, token, null);
            swipe.setRefreshing(false);
        });

        binding.searchViewTasksList.setImeOptions(EditorInfo.IME_ACTION_DONE);

        binding.fabListTasks.setOnClickListener(v ->
                this.startActivity(new Intent(getActivity(), AddTaskActivity.class)
                        .putExtra("id_user", id))
        );
        GetTaskListForCertainUser get = new GetTaskListForCertainUser(binding, this, this, getActivity());
        get.getTaskForUser(recycleView, id, token, null);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.ms.nameUserTasksList.setText(null);
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getDetailsUser(id, token).observe(getViewLifecycleOwner(), lt -> {
            if(lt != null){
                String count = "Кількість завдань: " + lt.getTaskDto().size();
                binding.ms.countTasksList.setText(count);
                ShapeableImageView imageContainer = binding.ms.imageViewTasksList;
                if(lt.getUserDto().getIcon()!= null){
                    FilesViewModel viewModel = new ViewModelProvider(this).get(FilesViewModel.class);
                    viewModel.getFiles(token, lt.getUserDto().getIcon()).observe(this, f -> {
                        if (f != null){
                            bytes = Base64.getDecoder().decode(f.getFile());
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageContainer.setImageBitmap(bitmap);
                        } else Toast.makeText(getContext(), ErrorEnum.CONNECTION_TO_INTERNET.getName(), Toast.LENGTH_SHORT).show();
                    });
                } else imageContainer.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_user, requireActivity().getTheme()));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}