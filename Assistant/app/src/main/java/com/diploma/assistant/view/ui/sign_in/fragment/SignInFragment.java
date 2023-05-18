package com.diploma.assistant.view.ui.sign_in.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.*;
import android.widget.LinearLayout;

import com.diploma.assistant.databinding.SignInFragmentBinding;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.ui.main_page.admin.NavigationActivity;
import com.diploma.assistant.view.ui.main_page.admin.UserNavigationActivity;
import com.diploma.assistant.view.ui.sign_in.activity.SignInChangePasswordFragment;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SignInFragment extends Fragment {
    protected SignInFragmentBinding binding;
    protected final LifecycleOwner lifecycleOwner = this;
    protected final ViewModelStoreOwner viewModelStoreOwner = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = SignInFragmentBinding.inflate(inflater, container, false);

        SignInViewModelImpl classes = new SignInViewModelImpl(getActivity(), this, this, getContext(), NavigationActivity.class, UserNavigationActivity.class);
            binding.buttonSignIn.setOnClickListener(view -> {
                String email = Objects.requireNonNull(binding.emailSignIn.getText()).toString();
                String password = Objects.requireNonNull(binding.passwordSignIn.getText()).toString();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                binding.buttonSignIn.setEnabled(false);
                binding.progressBar.setLayoutParams(params);
                binding.progressBar.setVisibility(View.VISIBLE);
                classes.getJwtTokensFromDataBase(email, password, binding);
            });
            binding.forgotPassword.setOnClickListener(view -> requireActivity().startActivity(new Intent(requireActivity(), SignInChangePasswordFragment.class)));
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}