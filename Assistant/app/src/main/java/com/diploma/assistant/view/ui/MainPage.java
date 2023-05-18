package com.diploma.assistant.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.diploma.assistant.databinding.FirsPageBinding;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.ui.main_page.admin.NavigationActivity;
import com.diploma.assistant.view.ui.main_page.admin.UserNavigationActivity;
import com.diploma.assistant.view.ui.sign_in.fragment.SignInViewModelImpl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
                AuthenticatorService service = new AuthenticatorService(this);
                String email = service.getTokenProperty(bearerToken).getBody().get("email", String.class);
                classes.getUserDetails(email, bearerToken);
            } catch (ExpiredJwtException e){
                accounts.clear("jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
                startActivity(new Intent(this, SwitchEntriesActivity.class));
            }

        } else startActivity(new Intent(this, SwitchEntriesActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    public boolean hasInternetAccess() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://www.google.com").openConnection();
            connection.setRequestProperty("User-Agent", "ConnectionTest");
            connection.setRequestProperty("Connection", "close");
            connection.setConnectTimeout(1500);
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkButtonInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}