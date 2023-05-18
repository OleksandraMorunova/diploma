package com.diploma.assistant.view.ui.sign_up.activity.sign_up_3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;

import com.diploma.assistant.databinding.SignUp3FragmentBinding;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.view.ui.MainPage;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_2.SendAndGetCode;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_2.SignUpCodeFragment;

import java.util.Objects;

public class SignUpSetLoginAndPasswordFragment extends AppCompatActivity {
    SignUp3FragmentBinding binding;
    LifecycleOwner lifecycleOwner = this;
    ViewModelStoreOwner viewModelStoreOwner = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignUp3FragmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String number = getIntent().getStringExtra("number");

        Listener listener = new Listener(binding);
        listener.listenerFieldsPasswordAndEmail();

        binding.buttonSignUp.setOnClickListener(s -> {
            User u = new User();
            u.setEmail(Objects.requireNonNull(binding.emailSignUp.getText()).toString());
            u.setPhone(number);
            if(!binding.textFieldPasswordTry.isHelperTextEnabled()){
                SendAndGetCode sendAndGetCode = new SendAndGetCode(viewModelStoreOwner, lifecycleOwner, SignUpSetLoginAndPasswordFragment.this);
                sendAndGetCode.post(u, Objects.requireNonNull(binding.passwordSignUp.getText()).toString(), SignUpCodeFragment.class);
            }
        });
    }

    public void onBackPressed(){
        Intent i = new Intent(SignUpSetLoginAndPasswordFragment.this, MainPage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
