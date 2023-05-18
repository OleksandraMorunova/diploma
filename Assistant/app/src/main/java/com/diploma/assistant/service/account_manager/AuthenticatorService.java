package com.diploma.assistant.service.account_manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

public class AuthenticatorService implements IAuthenticatorService<String>{
    private final Activity activity;

    public AuthenticatorService(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void savedTokenToSharedPreferences(String preferencesName, String key, Set<String> value){
        SharedPreferences editor = activity.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        editor.edit().putStringSet(key, value).apply();
    }

    @Override
    public void savedString(String preferencesName, String key, String value){
        SharedPreferences settings = activity.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        settings.edit().putString(key, value).apply();
    }

    @Override
    public void clear(String key, String preferencesName){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        settings.edit().remove(key).apply();
    }

    @Override
    public Set<String> getTokenFromSharedPreferences(String key, String preferencesName){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(key, new HashSet<>());
    }

    public String getStringFromSharedPreferences(String key, String preferencesName){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    @Override
    public String getElementFromSet(String kayValue, String key, String preferencesName){
        Set<String> jwtToken = getTokenFromSharedPreferences(key,preferencesName);
        if(jwtToken != null){
            for(String value : jwtToken){
                if(value.contains(kayValue)){
                    return value;
                }
                else Log.e("Shared Preferences is null", value);
            }
        }
        return null;
    }

    @Override
    public Jwt<Header, Claims> getTokenProperty(String token) {
        byte[] secret = "uu74l8S6ewO/Nmrh3waPdCfyF7UFTUtFoI44Z5c75X0=".getBytes(StandardCharsets.UTF_8);
        token = token.substring(7, token.length()-1);
        int i = token.lastIndexOf('.');
        String withoutSignature = token.substring(0, i+1);
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJwt(withoutSignature);
    }

}