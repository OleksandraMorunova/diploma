package com.diploma.assistant.view.ui.sign_in.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.SignInFragmentBinding;
import com.diploma.assistant.model.entity.regiastartion_and_resource_services.UserAndTasks;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.enumaration.ErrorEnum;
import com.diploma.assistant.model.enumaration.StatusUserEnum;
import com.diploma.assistant.model.enumaration.TypeUserEnum;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.service.firebase.MyFirebaseInstanceIDService;
import com.diploma.assistant.view_model.TokenViewModel;
import com.diploma.assistant.view_model.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SignInViewModelImpl implements Serializable{
    private final Class<?> classAdmin;
    private final Class<?> classUser;
    private final Context context;
    private final Set<String> tokenSet = new HashSet<>();
    private final Activity activity;
    private final LifecycleOwner lifecycleOwner;
    private final ViewModelStoreOwner viewModelStoreOwner;

    public SignInViewModelImpl(Activity activity, LifecycleOwner lifecycleOwner, ViewModelStoreOwner viewModelStoreOwner, Context context, Class<?> classAdmin, Class<?> classUser) {
        this.activity = activity;
        this.lifecycleOwner = lifecycleOwner;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.context = context;
        this.classAdmin = classAdmin;
        this.classUser = classUser;
    }

    public void getJwtTokensFromDataBase(String email, String password, SignInFragmentBinding binding){
        TokenViewModel tokenViewModel = new ViewModelProvider(viewModelStoreOwner).get(TokenViewModel.class);
        tokenViewModel.getToken(email, password).observe(lifecycleOwner, authResponseModel -> ((Activity) context).runOnUiThread(() -> {
            if(authResponseModel != null) {
                String token = authResponseModel.getType() + authResponseModel.getAccessToken();
                tokenSet.add(token);
                tokenSet.add("Refresh " + authResponseModel.getRefreshToken());
                AuthenticatorService authenticator = new AuthenticatorService(activity);
                authenticator.savedTokenToSharedPreferences("com.assistant.emmotechie.PREFERENCE_FILE_KEY", "jwt_token", tokenSet);
                getUserDetails(email, token);
            } else {
                binding.buttonSignIn.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Ви не зареєстровані", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void getUserDetails(String email, String token){
        AuthenticatorService authenticator = new AuthenticatorService(activity);
        UserViewModel userViewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
        userViewModel.getDetailsUser(email, token).observe(lifecycleOwner, userEntity -> ((Activity) context).runOnUiThread(() -> {
            if(userEntity != null){
                if(userEntity.getUserDto().getStatus().equals(StatusUserEnum.DEACTIVE.getStatus())){
                    User user = new User();
                    user.setStatus(StatusUserEnum.ACTIVE.getStatus());
                    userViewModel.updateUserDetails(userEntity.getUserDto().getId(), user, null).observe(lifecycleOwner, u -> {
                        if(u == null){
                            Log.e("Update statu user to ACTIVE", "Body is null");
                        }
                    });
                }
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.w("FIREBASE TOKEN", "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            String tokenFirebase = task.getResult();
                            Log.d("FIREBASE TOKEN", tokenFirebase);

                            User user = new User();
                            user.setUserTokenFirebase(tokenFirebase);
                            userViewModel.updateUserDetails(userEntity.getUserDto().getId(), user, null).observe(lifecycleOwner, o -> {});
                        });
                authenticator.savedString("com.assistant.emmotechie.PREFERENCE_FILE_KEY", "id_user", userEntity.getUserDto().getId());
                checkUserStatusForPermission(userEntity);
            } else {
                Toast.makeText(context, "Проблема з даними", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void checkUserStatusForPermission(UserAndTasks userEntity){
        boolean statusNotNull = userEntity.getUserDto().getStatus().equals(StatusUserEnum.ACTIVE.getStatus());
        String role = userEntity.getUserDto().getRoles()
                .stream().map(String::valueOf)
                .collect(Collectors.joining(" "));
        if(statusNotNull && role.equals(TypeUserEnum.USER.getTypeUserName())) {
            context.startActivity(new Intent(context, classUser));
        } else if(statusNotNull && role.equals(TypeUserEnum.ADMIN.getTypeUserName())){
            String roles =  userEntity.getUserDto().getRoles().stream().map(String::valueOf).collect(Collectors.joining(" "));
            context.startActivity(new Intent(context, classAdmin)
                    .putExtra("id", userEntity.getUserDto().getId())
                    .putExtra("role",  roles)
            );
        } else Toast.makeText(context, ErrorEnum.INVALID.getName(), Toast.LENGTH_SHORT).show();
    }
}