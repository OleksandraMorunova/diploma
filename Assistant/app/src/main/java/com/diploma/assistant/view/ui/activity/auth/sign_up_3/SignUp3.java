package com.diploma.assistant.view.ui.activity.auth.sign_up_3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.diploma.assistant.databinding.FragmentSignUp3Binding;
import com.diploma.assistant.model.entity.User;
import com.diploma.assistant.model.enumaration.ErrorEnum;
import com.diploma.assistant.model.enumaration.MakeToastEnum;
import com.diploma.assistant.view.ui.activity.auth.sign_in.SwitchEntriesActivity;
import com.diploma.assistant.view.ui.activity.auth.sign_up_2.CheckString;
import com.diploma.assistant.view.ui.activity.auth.sign_up_2.SendAndGetCode;
import com.diploma.assistant.view.ui.activity.auth.sign_up_2.SignUp2;
import com.diploma.assistant.view_model.UserViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SignUp3 extends AppCompatActivity {
    FragmentSignUp3Binding binding;
    LifecycleOwner lifecycleOwner = this;
    ViewModelStoreOwner viewModelStoreOwner = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSignUp3Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String number = getIntent().getStringExtra("number");


        EditText[] o = new EditText[]{ binding.passwordSignUp,  binding.passwordSignUpTry};
        List<EditText> passwordSignUp = Arrays.asList(o);

        for(int i = 0; i < passwordSignUp.size(); i++) {
            int finalI = i;
            passwordSignUp.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                   if(finalI == 0){
                        if(!CheckString.password(s.toString())){
                            binding.textFieldPassword.setHelperTextEnabled(true);
                            binding.textFieldPassword.setHelperText(ErrorEnum.PASSWORD.getName());
                        } else binding.textFieldPassword.setHelperTextEnabled(false);
                   } else if(finalI == 1){
                        if(s.length() > 0 && Objects.requireNonNull(binding.passwordSignUp.getText()).toString().equals(s.toString())){
                            binding.textFieldPasswordTry.setHelperTextEnabled(false);
                        } else {
                            binding.textFieldPasswordTry.setHelperTextEnabled(true);
                            binding.textFieldPasswordTry.setHelperText(ErrorEnum.NO_MATCH_PASSWORDS.getName());
                        }
                   }
                }
            });
        }

        binding.buttonSignUp.setOnClickListener(s -> {
            User u = new User();
            u.setEmail(Objects.requireNonNull(binding.emailSignUp.getText()).toString());
            u.setPhone(number);

            if(!binding.textFieldPasswordTry.isHelperTextEnabled()){
                SendAndGetCode sendAndGetCode = new SendAndGetCode(viewModelStoreOwner, lifecycleOwner, SignUp3.this);
                sendAndGetCode.post(u, Objects.requireNonNull(binding.passwordSignUp.getText()).toString());
            }
        });
    }

    public void onBackPressed(){
        Intent i = new Intent(SignUp3.this, SwitchEntriesActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
