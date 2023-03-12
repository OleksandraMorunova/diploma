package com.diploma.assistant.view.ui.activity.auth.sign_up_2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.FragmentSignUp2Binding;
import com.diploma.assistant.model.entity.User;
import com.diploma.assistant.view.ui.activity.auth.sign_in.SwitchEntriesActivity;
import com.diploma.assistant.view.ui.activity.auth.sign_up_3.SignUp3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SignUp2 extends AppCompatActivity{
    private FragmentSignUp2Binding binding;
    private Timer timer;

    ViewModelStoreOwner viewModelStoreOwner = this;
    LifecycleOwner lifecycleOwner = this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSignUp2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String number = getIntent().getStringExtra("number");

        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        System.out.println(email + password);

        if(email == null && password == null) {
            binding.textMessageNumber.setText(String.format(getResources().getString(R.string._1_s), number));
            SmsCheck smsCheck = new SmsCheck(SignUp2.this);
            smsCheck.getSms();
        }

        timer = new Timer(binding, SignUp2.this, lifecycleOwner, viewModelStoreOwner);
        timer.getCountDownTimer(number, email, password);


        //TODO Посилає код
        //SendAndGetCode code = new SendAndGetCode(viewModelStoreOwner, lifecycleOwner);
        //code.receiverCode(number.replace(" ", "").replace("-", ""));

        EditText[] o = new EditText[]{binding.editText1, binding.editText2, binding.editText3, binding.editText4, binding.editText5};
        List<EditText> eText = Arrays.asList(o);
        for(int i = 0; i < eText.size(); i++){
            int finalI = i;
            eText.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().length() > 0 && finalI != 4)  eText.get(finalI + 1).requestFocus();
                    if(s.toString().length() == 0 && finalI != 0) eText.get(finalI - 1).requestFocus();
                    if(finalI == 4) {
                        SendAndGetCode code = new SendAndGetCode(binding, SignUp2.this, viewModelStoreOwner, lifecycleOwner);
                        code.getCode(s.toString(), number, email, password);
                    }
                }
            });

            eText.get(i).setOnKeyListener((v, keyCode, event) -> {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    eText.get(finalI - 1).requestFocus();
                }
                return false;
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DEL){
            System.out.println(keyCode + "   " + event);
            Log.i("TAG", "Key pressed - KEYCODE_DEL");
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed(){
        Intent i = new Intent(SignUp2.this, SwitchEntriesActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        timer.cancelCountDownTimer();
    }
}