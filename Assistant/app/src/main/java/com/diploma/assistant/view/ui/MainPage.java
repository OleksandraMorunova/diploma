package com.diploma.assistant.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.diploma.assistant.databinding.FirsPageBinding;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.ui.main_page.admin.NavigationActivity;
import com.diploma.assistant.view.ui.main_page.user.UserNavigationActivity;
import com.diploma.assistant.view.ui.sign_in.fragment.SignInViewModelImpl;
import com.diploma.assistant.view_model.TokenViewModel;

import java.util.HashSet;
import java.util.Set;

import io.jsonwebtoken.ExpiredJwtException;

public class MainPage extends AppCompatActivity {
    private FirsPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FirsPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AuthenticatorService accounts = new AuthenticatorService(this);
        String bearerToken = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        SignInViewModelImpl classes = new SignInViewModelImpl(this, this, this, this, NavigationActivity.class, UserNavigationActivity.class);
        if(bearerToken != null){
            try {
                String email = accounts.getTokenProperty(bearerToken).getBody().get("email", String.class);
                classes.getUserDetails(email, bearerToken);
            } catch (ExpiredJwtException e){
                String refreshToken = accounts.getElementFromSet("Refresh", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
                TokenViewModel tokenViewModel = new ViewModelProvider(this).get(TokenViewModel.class);
                tokenViewModel.getNewAccessAndRefreshToken(refreshToken).observe(this, g -> {
                    if(g != null){
                        Set<String> tokenSet = new HashSet<>();
                        accounts.clear("jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
                        String token = "Bearer " + g.getAccessToken();
                        tokenSet.add(token);
                        tokenSet.add("Refresh " +g.getRefreshToken());
                        accounts.savedTokenToSharedPreferences("com.assistant.emmotechie.PREFERENCE_FILE_KEY", "jwt_token", tokenSet);
                    } else {
                        startActivity(new Intent(this, SwitchEntriesActivity.class));
                    }
                });
            }

        } else startActivity(new Intent(this, SwitchEntriesActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}