package com.diploma.assistant.view.ui.sign_up.activity.sign_up_2;

import com.diploma.assistant.databinding.SignUp2FragmentBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CheckStringLine {
    private SignUp2FragmentBinding binding;
    private final static String REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";

    public CheckStringLine(SignUp2FragmentBinding binding) {
        this.binding = binding;
    }

    public CheckStringLine(){}

    public boolean editTextIsNotNull(){
        List<Integer> l = Arrays.asList(binding.editText1.getText().length(), binding.editText2.getText().length(), binding.editText3.getText().length(), binding.editText4.getText().length(), binding.editText5.getText().length());
        return l.stream().anyMatch(Objects::nonNull);
    }

    public boolean editTextIsNumber(){
        List<String> l = Arrays.asList(binding.editText1.getText().toString(), binding.editText2.getText().toString(), binding.editText3.getText().toString(), binding.editText4.getText().toString(), binding.editText5.getText().toString());
        return l.stream().anyMatch(this::method2);
    }

    public boolean method2(String maybeNumber){
        if(!maybeNumber.equals("")){
            return maybeNumber.chars().allMatch(Character::isDigit);
        } else return false;
    }

    public static boolean password(String password){
        return password.matches(REGEX);
    }

    public static String parserData(String data){
        return LocalDateTime.parse(data).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }
}
