package com.diploma.assistant.view.ui.activity.auth.sign_up_2;

import android.content.Context;
import android.os.CountDownTimer;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.FragmentSignUp2Binding;
import com.diploma.assistant.model.entity.User;

public class Timer {
    private final FragmentSignUp2Binding binding;
    private final Context context;

    private final LifecycleOwner lifecycleOwner;
    private final ViewModelStoreOwner viewModelStoreOwner;

    private CountDownTimer countDownTimer;

    public Timer(FragmentSignUp2Binding binding, Context context, LifecycleOwner lifecycleOwner, ViewModelStoreOwner viewModelStoreOwner) {
        this.binding = binding;
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.viewModelStoreOwner = viewModelStoreOwner;
    }

    public void getCountDownTimer(String number, String email, String password){
        SendAndGetCode code = new SendAndGetCode(binding, context, viewModelStoreOwner, lifecycleOwner);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) millisUntilFinished / 1000;
                int minutes = seconds / 60;
                binding.timer.setText(String.format(context.getResources().getString(R.string._1_x_2_d), minutes, seconds));
            }

            @Override
            public void onFinish() {
                binding.timer.setText("Надіслати повідомлення");
                if(email == null && password == null){
                    binding.timer.setOnClickListener(view -> code.receiverCode(number));
                } else {
                    User u = new User();
                    u.setEmail(email); u.setPhone(number);
                    binding.timer.setOnClickListener(view -> code.post(u, password));
                }
            }
        }.start();
    }

    public void cancelCountDownTimer(){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }
}
