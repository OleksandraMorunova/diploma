package com.diploma.assistant.view.ui.fragment.auth.sign_up;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelStoreOwner;

import com.diploma.assistant.databinding.FragmentSignUp1Binding;
import com.diploma.assistant.model.enumaration.ErrorEnum;
import com.google.android.gms.auth.api.identity.Identity;

import java.util.Objects;

public class SignUp1 extends Fragment {
    private FragmentSignUp1Binding binding;
    private SignUp1Add operation;
    ViewModelStoreOwner viewModelStoreOwner = this;

    ActivityResultLauncher<IntentSenderRequest> phoneNumberHintIntentResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                        try {
                            String phoneNumber = Identity.getSignInClient(requireContext()).getPhoneNumberFromIntent(result.getData());
                            binding.phoneNumberSignUp.setText(phoneNumber);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUp1Binding.inflate(inflater, container, false);

        operation = new SignUp1Add(requireContext());
        operation.requestingPhoneNumberHint(phoneNumberHintIntentResultLauncher);

        binding.phoneNumberSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                binding.phoneNumberSignUp.removeTextChangedListener(this);
                try {
                    binding.textFieldPhoneNumber.setHelperTextEnabled(false);
                    String isoCode = operation.getCountryIsoCodeByPhoneNumber(s.toString());
                    String new_number = operation.formattedNumber(s.toString());

                    if (isoCode != null && new_number != null) {
                        int resourceId = getResources().getIdentifier(
                                "ic_" + isoCode,
                                "drawable",
                                requireContext().getPackageName());

                        binding.textFieldPhoneNumber.setStartIconDrawable(resourceId);
                        Objects.requireNonNull(binding.phoneNumberSignUp.getText()).clear();
                        binding.phoneNumberSignUp.append(new_number);
                        binding.phoneNumberSignUp.setSelection(binding.phoneNumberSignUp.getText().length());
                    }
                    else {
                        binding.textFieldPhoneNumber.setStartIconDrawable(null);
                        binding.textFieldPhoneNumber.setHelperTextEnabled(true);
                        binding.textFieldPhoneNumber.setHelperText(ErrorEnum.INVALID_COUNTRY_CODE.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                binding.phoneNumberSignUp.addTextChangedListener(this);
            }
        });

        operation = new SignUp1Add(requireContext(), getViewLifecycleOwner(), viewModelStoreOwner);

        binding.buttonSignUp.setOnClickListener(view -> {
            String number = (Objects.requireNonNull(binding.phoneNumberSignUp.getText())).toString().replace(" ", "").replace("-", "");
            operation.requestUserByPhoneNumber(number);
        });

        return binding.getRoot();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
