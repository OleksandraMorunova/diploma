package com.diploma.assistant.view.ui.sign_up.activity.sign_up_2;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.SignUp2FragmentBinding;
import com.diploma.assistant.model.entity.registration_service.User;

import java.util.List;

public class Timer {
    private final SignUp2FragmentBinding binding;
    private final Context context;

    private final LifecycleOwner lifecycleOwner;
    private final ViewModelStoreOwner viewModelStoreOwner;

    private CountDownTimer countDownTimer;

    public Timer(SignUp2FragmentBinding binding, Context context, LifecycleOwner lifecycleOwner, ViewModelStoreOwner viewModelStoreOwner) {
        this.binding = binding;
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.viewModelStoreOwner = viewModelStoreOwner;
    }

    public void getCountDownTimer(List<Object> l, TextView timer){
        String number = (String) l.get(0);
        String email = (String) l.get(1);
        String password = (String) l.get(2);

        SendAndGetCode code = new SendAndGetCode(binding, context, viewModelStoreOwner, lifecycleOwner);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) millisUntilFinished / 1000;
                int minutes = seconds / 60;
                timer.setText(String.format(context.getResources().getString(R.string._1_x_2_d), minutes, seconds));
            }

            @Override
            public void onFinish() {
                timer.setText("Надіслати повідомлення");
                if(email == null){
                    timer.setOnClickListener(view -> code.receiverCode(number));
                } else {
                    User u = new User();
                    u.setEmail(email); u.setPhone(number);
                   timer.setOnClickListener(view -> code.post(u, password, SignUpCodeFragment.class));
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
