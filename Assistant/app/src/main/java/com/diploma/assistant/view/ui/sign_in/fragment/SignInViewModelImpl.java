package com.diploma.assistant.view.ui.sign_in.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.diploma.assistant.databinding.SignInFragmentBinding;
import com.diploma.assistant.model.entity.regiastartion_and_resource_services.UserAndTasks;
import com.diploma.assistant.model.enumaration.ErrorEnum;
import com.diploma.assistant.model.enumaration.StatusUserEnum;
import com.diploma.assistant.model.enumaration.TypeUserEnum;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view_model.TokenViewModel;
import com.diploma.assistant.view_model.UserViewModel;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SignInViewModelImpl implements Serializable{
    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private final SignInFragmentBinding binding;

    private final Class<?> classAdmin;
    private final Class<?> classUser;

    private final Set<String> tokenSet = new HashSet<>();

    public SignInViewModelImpl(Context context, LifecycleOwner lifecycleOwner, ViewModelStoreOwner viewModelStoreOwner, SignInFragmentBinding binding, Class<?> classAdmin, Class<?> classUser) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.binding = binding;
        this.classAdmin = classAdmin;
        this.classUser = classUser;
    }

    public void getJwtTokensFromDataBase(String email, String password){
        TokenViewModel tokenViewModel = new ViewModelProvider(viewModelStoreOwner).get(TokenViewModel.class);
        tokenViewModel.getToken(email, password).observe(lifecycleOwner, authResponseModel -> ((Activity) context).runOnUiThread(() -> {
            if(authResponseModel != null) {
                String token = Objects.requireNonNull(authResponseModel.getType()) + Objects.requireNonNull(authResponseModel.getAccessToken());
                AuthenticatorService authenticator = new AuthenticatorService(context);
                tokenSet.add(token);
                tokenSet.add("Refresh " + authResponseModel.getRefreshToken());
                authenticator.savedTokenToSharedPreferences("com.assistant.emmotechie.PREFERENCE_FILE_KEY", "jwt_token", tokenSet);
                getUserDetails(email, token);
            } else {
                binding.buttonSignIn.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Проблема з токеном", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void getUserDetails(String email, String token){
        UserViewModel userViewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
        userViewModel.getDetailsUser(email, token).observe(lifecycleOwner, userEntity -> ((Activity) context).runOnUiThread(() -> {
            if(userEntity != null){
                binding.progressBar.setVisibility(View.GONE);
                checkUserStatusForPermission(token, userEntity);
            } else {
                binding.buttonSignIn.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Проблема з даними", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void checkUserStatusForPermission(String token, UserAndTasks userEntity){
        boolean statusNotNull = userEntity.getUserDto().getStatus().equals(StatusUserEnum.ACTIVE.getStatus());
        String role = userEntity.getUserDto().getRoles().stream().map(String::valueOf).collect(Collectors.joining(" "));
        if(statusNotNull && role.equals(TypeUserEnum.USER.getTypeUserName())) {
            context.startActivity(new Intent(context, classUser));
        } else if(statusNotNull && role.equals(TypeUserEnum.ADMIN.getTypeUserName())){
            String roles =  userEntity.getUserDto().getRoles().stream().map(String::valueOf).collect(Collectors.joining(" "));
            context.startActivity(new Intent(context, classAdmin)
                    .putExtra("token", token)
                    .putExtra("name", userEntity.getUserDto().getName())
                    .putExtra("phone", userEntity.getUserDto().getPhone())
                    .putExtra("role",  roles)
                    .putExtra("icon", userEntity.getUserDto().getIcon())
            );
        } else Toast.makeText(context, ErrorEnum.INVALID.getName(), Toast.LENGTH_SHORT).show();
    }
}