package com.diploma.assistant.view.ui.sign_in.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;

import com.diploma.assistant.databinding.SignUp3FragmentBinding;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_2.SendAndGetCode;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_2.SignUpCodeFragment;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_3.Listener;

import java.util.Objects;

public class SignInChangePasswordFragment extends AppCompatActivity {
    SignUp3FragmentBinding binding;
    LifecycleOwner lifecycleOwner = this;
    ViewModelStoreOwner viewModelStoreOwner = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignUp3FragmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Listener listener = new Listener(binding);
        listener.listenerFieldsPasswordAndEmail();

        binding.buttonSignUp.setOnClickListener(s -> {
            User u = new User();
            u.setEmail(Objects.requireNonNull(binding.emailSignUp.getText()).toString());
            if(!binding.textFieldPasswordTry.isHelperTextEnabled()){
                SendAndGetCode sendAndGetCode = new SendAndGetCode(viewModelStoreOwner, lifecycleOwner, SignInChangePasswordFragment.this);
                sendAndGetCode.checkEmailViewModelImplMethod(u, Objects.requireNonNull(binding.passwordSignUp.getText()).toString(), SignUpCodeFragment.class);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
