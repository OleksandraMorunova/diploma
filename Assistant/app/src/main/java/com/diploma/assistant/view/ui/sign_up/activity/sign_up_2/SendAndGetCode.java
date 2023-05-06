package com.diploma.assistant.view.ui.sign_up.activity.sign_up_2;

import android.content.*;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.*;

import com.diploma.assistant.databinding.SignUp2FragmentBinding;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.enumaration.MakeToastEnum;
import com.diploma.assistant.view_model.SmsViewModel;
import com.diploma.assistant.view_model.UserViewModel;

import java.util.*;

public class SendAndGetCode {
    private SignUp2FragmentBinding fragmentSignUp2Binding;
    private final Context context;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private final LifecycleOwner lifecycleOwner;

    public SendAndGetCode(SignUp2FragmentBinding binding, Context context, ViewModelStoreOwner viewModelStoreOwner, LifecycleOwner lifecycleOwner) {
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
        SmsViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(SmsViewModel.class);
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

    public void post(User u, String password, Class<?> o){
        SmsViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(SmsViewModel.class);
        viewModel.postSms(u).observe(lifecycleOwner, user -> {
            try{
                System.out.println(user);
                if(user != null){
                    Log.i("TAG", "Success receiver email code");
                    context.startActivity(new Intent(context, o).putExtra("number", u.getPhone()).putExtra("email", u.getEmail()).putExtra("password", password));
                }  else {
                    Log.e("TAG", "Помилка відправлення коду на пошту");
                }
            } catch (Exception e){
                Log.e("TAG", "Failure receiver code");
                Toast.makeText(context, MakeToastEnum.NO_CONNECTION_WITH_SERVER.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCode(User u, List<Object> userData, Class<?> phoneClass, Class<?> emailClass){
        SmsViewModel smsViewModel = new ViewModelProvider(viewModelStoreOwner).get(SmsViewModel.class);
        smsViewModel.getCode(u).observe(lifecycleOwner, user -> {
            try{
                if(user != null){
                   checkByUpdateUserDetailsPhoneOrEmail(userData, phoneClass, emailClass);
                } else {
                    clearEditText();
                    Toast.makeText(context, MakeToastEnum.NO_RIGHT_CODE.getError(), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Log.e("TAG", "Failure receiver code");
                Toast.makeText(context, MakeToastEnum.NO_CONNECTION_WITH_SERVER.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkEmailViewModelImplMethod(User user, String password, Class<?> emailClass){
        UserViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
        viewModel.checkUserEmail(user.getEmail()).observe(lifecycleOwner, check -> {
            try {
                if(check != null){
                    post(user, password, emailClass);
                    Log.i("TAG", "Email founded");
                } else Toast.makeText(context, MakeToastEnum.NO_CONNECTION_WITH_SERVER.getError(), Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Log.e("TAG", "Failure founded email");
                Toast.makeText(context, "Пошта не існує", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkByUpdateUserDetailsPhoneOrEmail(List<Object> userData, Class<?> phoneClass, Class<?> emailClass){
        String number = (String) userData.get(0);
        String email = (String) userData.get(1);
        String password = (String) userData.get(2);

        if(password == null && email == null){
            context.startActivity(new Intent(context, phoneClass).putExtra("number", number));
        } else if(password != null && email != null && number != null) {
            User new_u = new User();
            new_u.setEmail(email);
            new_u.setPassword(password);
            updateDetailsByEmailViewModelImplMethod(new_u, number, emailClass);
        } else {
            User new_u = new User();
            new_u.setPassword(password);
            checkEmailViewModelImplMethod(new_u, email, emailClass);
            updateDetailsByEmailViewModelImplMethod(new_u, email, emailClass);
        }
    }

    private void updateDetailsByEmailViewModelImplMethod(User new_u, String numberOrEmail, Class<?> emailClass){
        UserViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
        viewModel.updateUserDetails(numberOrEmail, new_u).observe(lifecycleOwner, users -> {
            try {
                if(users != null){
                    Log.i("TAG", "Updated user data by email and password");
                    Toast.makeText(context, MakeToastEnum.UPDATE_DETAILS.getError(), Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, emailClass));
                } else Toast.makeText(context, MakeToastEnum.NO_CONNECTION_WITH_SERVER.getError(), Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Log.e("TAG", "Failure update email and password");
                Toast.makeText(context, MakeToastEnum.NO_CONNECTION_WITH_SERVER.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearEditText(){
        fragmentSignUp2Binding.editText1.getText().clear(); fragmentSignUp2Binding.editText2.getText().clear();
        fragmentSignUp2Binding.editText3.getText().clear(); fragmentSignUp2Binding.editText4.getText().clear(); fragmentSignUp2Binding.editText5.getText().clear();
        fragmentSignUp2Binding.editText1.requestFocus();
    }
}
