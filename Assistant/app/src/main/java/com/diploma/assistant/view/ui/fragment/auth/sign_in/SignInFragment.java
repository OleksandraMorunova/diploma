package com.diploma.assistant.view.ui.fragment.auth.sign_in;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.*;
import android.widget.Toast;

import com.diploma.assistant.model.enumaration.*;
import com.diploma.assistant.databinding.FragmentSignInBinding;
import com.diploma.assistant.view.ui.activity.menu.ContentActivity;
import com.diploma.assistant.view.ui.activity.user.UsersActivity;
import com.diploma.assistant.view_model.UserViewModel;

import java.util.Objects;
import java.util.stream.Collectors;

public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;
    private UserViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);

        binding.buttonSignIn.setOnClickListener(view -> {
            String email = Objects.requireNonNull(binding.emailSignIn.getText()).toString();
            String password = Objects.requireNonNull(binding.passwordSignIn.getText()).toString();
            viewModelBinding(email,password);
        });
        return binding.getRoot();
    }

    private void viewModelBinding(String email, String password){
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getToken(email, password).observe(getViewLifecycleOwner(), authResponseModel -> {
          try {
              String token =  Objects.requireNonNull(authResponseModel.getType()) +  Objects.requireNonNull(authResponseModel.getAccessToken());

              viewModel.getDetailsByEmail(email, token).observe(getViewLifecycleOwner(), userEntity -> {
                  boolean statusNotNull = userEntity.getStatus().equals(StatusUserEnum.ACTIVE.getStatus());
                  String role = userEntity.getRoles().stream().map(String::valueOf).collect(Collectors.joining(" "));
                  if(statusNotNull && role.equals(TypeUserEnum.USER.getTypeUserName())) {
                      requireActivity().startActivity(new Intent(requireActivity(), ContentActivity.class));
                  } else if(statusNotNull && role.equals(TypeUserEnum.ADMIN.getTypeUserName())){
                      requireActivity().startActivity(new Intent(requireActivity(), UsersActivity.class));
                  } else Toast.makeText(requireActivity(), ErrorEnum.INVALID.getName(), Toast.LENGTH_SHORT).show();
              });
          } catch (Exception e){
              Toast.makeText(requireContext(), "Немає доступу", Toast.LENGTH_SHORT).show();
          }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}