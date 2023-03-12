package com.diploma.assistant.view.ui.activity.auth.sign_up_2;

import com.diploma.assistant.databinding.FragmentSignUp2Binding;

public class CheckString {
    private FragmentSignUp2Binding binding;
    private final static String REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";

    public CheckString(FragmentSignUp2Binding binding) {
        this.binding = binding;
    }

    public CheckString(){}

    public boolean editTextIsNotNull(){
        return binding.editText1.getText().length() != 0 && binding.editText2.getText().length() != 0
                && binding.editText3.getText().length() != 0 && binding.editText4.getText().length() != 0
                && binding.editText5.getText().length() != 0;
    }

    public boolean editTextIsNumber(){
        return method2(binding.editText1.getText().toString()) && method2(binding.editText2.getText().toString())
                && method2(binding.editText3.getText().toString()) && method2(binding.editText4.getText().toString()) && method2(binding.editText5.getText().toString());
    }

    public boolean method2(String maybeNumber){
        if(!maybeNumber.equals("")){
            return maybeNumber.chars().allMatch(Character::isDigit);
        } else return false;
    }

    public static boolean password(String password){
        return password.matches(REGEX);
    }
}
