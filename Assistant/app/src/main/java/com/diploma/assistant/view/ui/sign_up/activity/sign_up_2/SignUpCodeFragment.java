package com.diploma.assistant.view.ui.sign_up.activity.sign_up_2;

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
import com.diploma.assistant.databinding.SignUp2FragmentBinding;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.view.ui.sign_in.activity.SwitchEntriesActivity;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_3.SignUpSetLoginAndPasswordFragment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SignUpCodeFragment extends AppCompatActivity{
    SignUp2FragmentBinding binding;
    private Timer timer;

    ViewModelStoreOwner viewModelStoreOwner = this;
    LifecycleOwner lifecycleOwner = this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignUp2FragmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String number = getIntent().getStringExtra("number");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        if(email == null && password == null) {
            binding.textMessageNumber.setText(String.format(getResources().getString(R.string.phone_number), number));
            SmsCheck smsCheck = new SmsCheck(SignUpCodeFragment.this);
            smsCheck.getSms();
        } else binding.textMessageNumber.setText(String.format(getResources().getString(R.string.email), email));

        timer = new Timer(binding, SignUpCodeFragment.this, lifecycleOwner, viewModelStoreOwner);
        List<Object> emailAndPassword = Arrays.asList(number, email, password);
        timer.getCountDownTimer(emailAndPassword, binding.timer);

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
                        SendAndGetCode code = new SendAndGetCode(binding, SignUpCodeFragment.this, viewModelStoreOwner, lifecycleOwner);
                        CheckStringLine checkString = new CheckStringLine(binding);
                        if(s.length() != 0 && checkString.editTextIsNotNull() && checkString.editTextIsNumber()) {
                            List<String> l = Arrays.asList(binding.editText1.getText().toString(), binding.editText2.getText().toString(), binding.editText3.getText().toString(), binding.editText4.getText().toString(), binding.editText5.getText().toString());
                            User u = new User();
                            u.setCode(l.stream().map(String::valueOf).collect(Collectors.joining("", "", "")));
                            code.getCode(u, emailAndPassword, SignUpSetLoginAndPasswordFragment.class, SwitchEntriesActivity.class);
                        }
                    }
                }
            });

            eText.get(i).setOnKeyListener((v, keyCode, event) -> {
                if(keyCode == KeyEvent.KEYCODE_DEL && finalI < 5){
                    eText.get(finalI - 1).requestFocus();
                }
                return false;
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DEL){
            Log.i("TAG", "Key pressed - KEYCODE_DEL");
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed(){
        Intent i = new Intent(SignUpCodeFragment.this, SwitchEntriesActivity.class);
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