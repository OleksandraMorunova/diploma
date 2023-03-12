package com.diploma.assistant.view.ui.activity.auth.sign_up_2;

import android.content.*;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.*;

import com.diploma.assistant.databinding.FragmentSignUp2Binding;
import com.diploma.assistant.model.entity.User;
import com.diploma.assistant.model.enumaration.MakeToastEnum;
import com.diploma.assistant.view.ui.activity.auth.sign_in.SwitchEntriesActivity;
import com.diploma.assistant.view.ui.activity.auth.sign_up_3.SignUp3;
import com.diploma.assistant.view_model.UserViewModel;

import java.util.*;
import java.util.stream.Collectors;

public class SendAndGetCode {
    private FragmentSignUp2Binding fragmentSignUp2Binding;

    private final Context context;

    private final ViewModelStoreOwner viewModelStoreOwner;
    private final LifecycleOwner lifecycleOwner;

    public SendAndGetCode(FragmentSignUp2Binding binding, Context context, ViewModelStoreOwner viewModelStoreOwner, LifecycleOwner lifecycleOwner) {
        this.fragmentSignUp2Binding = binding;
        this.context = context;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.lifecycleOwner = lifecycleOwner;
    }

    public SendAndGetCode(ViewModelStoreOwner viewModelStoreOwner, LifecycleOwner lifecycleOwner, Context context) {
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.lifecycleOwner = lifecycleOwner;
        this.context = context;
    }

    public void receiverCode(String number){
        UserViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
        User u = new User();
        u.setPhone(number);
        viewModel.createCode(u).observe(lifecycleOwner, userEntity -> {
            try{
                if(userEntity != null){
                    Log.e("TAG", "Success receiver code");
                }
            } catch (Exception e){
                Log.e("TAG", "Failure receiver code");
            }
        });
    }

    public void post(User u, String password){
        System.out.println(u.getEmail() + "" + u.getPhone());
        UserViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
        viewModel.postSms(u).observe(lifecycleOwner, user -> {
            try{
                if(user != null){
                    Log.e("TAG", "Success receiver code");
                    context.startActivity(new Intent(context, SignUp2.class).putExtra("number", u.getPhone()).putExtra("email", u.getEmail()).putExtra("password", password));
                }  else {
                    Log.e("TAG", "Помилка відправлення коду на пошту");
                }
            } catch (Exception e){
                Log.e("TAG", "Failure receiver code");
                Toast.makeText(context, MakeToastEnum.NO_CONNECTION_WITH_SERVER.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCode(String s, String phoneNumber, String email, String password){
        CheckString checkString = new CheckString(fragmentSignUp2Binding);
        if(s.length() != 0 && checkString.editTextIsNotNull() && checkString.editTextIsNumber()){
            List<String> l = Arrays.asList(fragmentSignUp2Binding.editText1.getText().toString(), fragmentSignUp2Binding.editText2.getText().toString(), fragmentSignUp2Binding.editText3.getText().toString(),
                    fragmentSignUp2Binding.editText4.getText().toString(), fragmentSignUp2Binding.editText5.getText().toString());
            UserViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
            User u = new User();
            u.setCode(l.stream().map(String::valueOf).collect(Collectors.joining("", "", "")));
            viewModel.getCode(u).observe(lifecycleOwner, user -> {
                try{
                    if(user != null){
                       if(password == null && email == null){
                           context.startActivity(new Intent(context, SignUp3.class).putExtra("number", phoneNumber));
                       } else {
                           User new_u = new User();
                           new_u.setEmail(email);
                           new_u.setPassword(password);
                           viewModel.updateUserDetails(phoneNumber, new_u).observe(lifecycleOwner, users -> {
                               try {
                                   if(users != null){
                                       Toast.makeText(context, MakeToastEnum.UPDATE_DETAILS.getError(), Toast.LENGTH_SHORT).show();
                                       context.startActivity(new Intent(context, SwitchEntriesActivity.class));
                                   } else  Toast.makeText(context, MakeToastEnum.NO_CONNECTION_WITH_SERVER.getError(), Toast.LENGTH_SHORT).show();
                               } catch (Exception e){
                                   Log.e("TAG", "Failure update email and password");
                                   Toast.makeText(context, MakeToastEnum.NO_CONNECTION_WITH_SERVER.getError(), Toast.LENGTH_SHORT).show();
                               }
                           });
                       }
                    } else {
                        fragmentSignUp2Binding.editText1.getText().clear(); fragmentSignUp2Binding.editText2.getText().clear();
                        fragmentSignUp2Binding.editText3.getText().clear(); fragmentSignUp2Binding.editText4.getText().clear(); fragmentSignUp2Binding.editText5.getText().clear();
                        fragmentSignUp2Binding.editText1.requestFocus();
                        Toast.makeText(context, MakeToastEnum.NO_RIGHT_CODE.getError(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.e("TAG", "Failure receiver code");
                    Toast.makeText(context, MakeToastEnum.NO_CONNECTION_WITH_SERVER.getError(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
