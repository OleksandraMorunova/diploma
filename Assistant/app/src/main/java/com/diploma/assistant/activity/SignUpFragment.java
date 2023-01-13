package com.diploma.assistant.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.FragmentSignUpBinding;
import com.diploma.assistant.model.RegistrationUser;
import com.google.android.material.textfield.TextInputEditText;


public class SignUpFragment extends Fragment {
    private TextInputEditText firstName, lastName, phone, email, password;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }
}