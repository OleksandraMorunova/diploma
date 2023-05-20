package com.diploma.assistant.view.ui.sign_in.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;

import android.provider.Settings;
import android.view.*;
import android.widget.LinearLayout;

import com.diploma.assistant.databinding.SignInFragmentBinding;
import com.diploma.assistant.view.ui.main_page.admin.NavigationActivity;
import com.diploma.assistant.view.ui.main_page.user.UserNavigationActivity;
import com.diploma.assistant.view.ui.sign_in.activity.SignInChangePasswordFragment;

import java.util.Objects;

public class SignInFragment extends Fragment {
    protected SignInFragmentBinding binding;
    protected final LifecycleOwner lifecycleOwner = this;
    protected final ViewModelStoreOwner viewModelStoreOwner = this;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    redirectToActiveForSavingData();
                } else {
                    openAppSettings();
                }
            });

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                redirectToActiveForSavingData();
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                openAppSettings();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = SignInFragmentBinding.inflate(inflater, container, false);

        binding.buttonSignIn.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= 33) {
                if (!(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)) {
                    askNotificationPermission();
                } else {
                    redirectToActiveForSavingData();
                }
            } else {
                redirectToActiveForSavingData();
            }
        });
        binding.forgotPassword.setOnClickListener(view -> requireActivity().startActivity(new Intent(requireActivity(), SignInChangePasswordFragment.class)));
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void redirectToActiveForSavingData(){
        String email = Objects.requireNonNull(binding.emailSignIn.getText()).toString();
        String password = Objects.requireNonNull(binding.passwordSignIn.getText()).toString();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        binding.buttonSignIn.setEnabled(false);
        binding.progressBar.setLayoutParams(params);
        binding.progressBar.setVisibility(View.VISIBLE);
        SignInViewModelImpl classes = new SignInViewModelImpl(getActivity(), this, this, getContext(), NavigationActivity.class, UserNavigationActivity.class);
        classes.getJwtTokensFromDataBase(email, password, binding);
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", requireContext().getPackageName(), null));
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}