package com.diploma.assistant.view.ui.sign_up.activity.sign_up_3;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.diploma.assistant.databinding.SignUp3FragmentBinding;
import com.diploma.assistant.model.enumaration.ErrorEnum;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_2.CheckStringLine;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Listener {
    private final SignUp3FragmentBinding binding;

    public Listener(SignUp3FragmentBinding binding) {
        this.binding = binding;
    }

    public void listenerFieldsPasswordAndEmail(){
        EditText[] o = new EditText[]{binding.passwordSignUp, binding.passwordSignUpTry};
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
                        if(!CheckStringLine.password(s.toString())){
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
    }
}
