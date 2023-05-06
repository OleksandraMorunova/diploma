package com.diploma.assistant.view.ui.sign_in.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;

import com.diploma.assistant.databinding.SignInFragmentBinding;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.ui.navigation.NavigationActivity;
import com.diploma.assistant.view.ui.sign_in.activity.SignInChangePasswordFragment;
import com.diploma.assistant.view.ui.user.UsersActivity;
import com.diploma.assistant.view_model.TokenViewModel;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

public class SignInFragment extends Fragment {
    private SignInFragmentBinding binding;
    private final LifecycleOwner lifecycleOwner = this;
    private final ViewModelStoreOwner viewModelStoreOwner = this;

    private final Set<String> tokenSet = new HashSet<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = SignInFragmentBinding.inflate(inflater, container, false);

        AuthenticatorService accounts = new AuthenticatorService(requireContext());
        String bearerToken = accounts.getElementFromSet("Bearer", "jwt_token","com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        SignInViewModelImpl classes = new SignInViewModelImpl(requireActivity(), lifecycleOwner, viewModelStoreOwner, binding, NavigationActivity.class, UsersActivity.class);
        if(bearerToken != null){
            try{
                AuthenticatorService service =  new AuthenticatorService(getContext());
                String email = service.getTokenProperty(bearerToken).getBody().get("email", String.class);
                classes.getUserDetails(email, bearerToken);
            } catch (ExpiredJwtException e){
                Log.e("ExpiredJwtException", " Difference of milliseconds");
                String refreshToken = accounts.getElementFromSet("Refresh","jwt_token","com.assistant.emmotechie.PREFERENCE_FILE_KEY");
                TokenViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(TokenViewModel.class);
                viewModel.getNewAccessToken(refreshToken).observe(lifecycleOwner, getToken -> {
                    if (getToken != null) {
                        accounts.clear("jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
                        tokenSet.add(getToken.getType() + getToken.getAccessToken());
                        tokenSet.add("Refresh " + getToken.getRefreshToken());
                        accounts.savedTokenToSharedPreferences("com.assistant.emmotechie.PREFERENCE_FILE_KEY", "jwt_token", tokenSet);
                    }
                });
            }
        }
            binding.buttonSignIn.setOnClickListener(view -> {
                String email = Objects.requireNonNull(binding.emailSignIn.getText()).toString();
                String password = Objects.requireNonNull(binding.passwordSignIn.getText()).toString();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                binding.buttonSignIn.setEnabled(false);
                binding.progressBar.setLayoutParams(params);
                binding.progressBar.setVisibility(View.VISIBLE);
                classes.getJwtTokensFromDataBase(email, password);
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