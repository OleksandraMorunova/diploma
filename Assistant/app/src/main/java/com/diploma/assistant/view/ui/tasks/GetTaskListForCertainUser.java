package com.diploma.assistant.view.ui.tasks;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.databinding.TasksListActivityFinallyBinding;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.model.enumaration.ErrorEnum;
import com.diploma.assistant.model.enumaration.TypeUserEnum;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.adapter.RecycleViewTasks;
import com.diploma.assistant.view_model.UserViewModel;

import java.util.List;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class GetTaskListForCertainUser {
    private final TasksListActivityFinallyBinding binding;
    private final LifecycleOwner lifecycle;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private final Activity activity;
    private List<TaskDto> listTask;
    private RecycleViewTasks adapter;

    public GetTaskListForCertainUser(TasksListActivityFinallyBinding binding, LifecycleOwner lifecycle, ViewModelStoreOwner viewModelStoreOwner, Activity activity) {
        this.binding = binding;
        this.lifecycle = lifecycle;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.activity = activity;
    }

    public void getTaskForUser(RecyclerView recycleView, String id, String token, String name){
        UserViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
        viewModel.getDetailsUser(id, token).observe(lifecycle, u -> {
            if(u != null){
                listTask = u.getTaskDto();
                recycleView.setHasFixedSize(true);
                recycleView.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext()));
                if(listTask != null){
                    adapter = new RecycleViewTasks(activity,  listTask, id, viewModelStoreOwner, lifecycle, name);
                    recycleView.setAdapter(adapter);

                    AuthenticatorService accounts = new AuthenticatorService(activity);
                    String bearerToken = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
                    SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode("uu74l8S6ewO/Nmrh3waPdCfyF7UFTUtFoI44Z5c75X0="));
                    Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(bearerToken.replace("Bearer ", "")).getBody();
                    List<String> role = (List<String>) claims.get("role");
                    if(role.get(0).equals(TypeUserEnum.ADMIN.getTypeUserName())){
                        binding.searchViewTasksList.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) { return false; }
                            @Override
                            public boolean onQueryTextChange(String newText) {
                                adapter.getFilter().filter(newText);
                                return false;
                            }
                        });
                    } else {
                        binding.searchViewTasksList.setVisibility(View.INVISIBLE);
                    }
                } else Toast.makeText(activity.getApplicationContext(), ErrorEnum.CONNECTION_TO_INTERNET.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}