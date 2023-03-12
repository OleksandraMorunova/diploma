package com.diploma.assistant.view.ui.fragment.auth.sign_up;

import android.content.*;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.result.*;
import androidx.lifecycle.*;

import com.diploma.assistant.model.enumaration.MakeToastEnum;
import com.diploma.assistant.view.ui.activity.auth.sign_up_2.SignUp2;
import com.diploma.assistant.view_model.UserViewModel;
import com.google.android.gms.auth.api.identity.*;
import com.google.i18n.phonenumbers.*;

import java.util.Objects;

public class SignUp1Add {
    private final Context context;
    private LifecycleOwner lifecycleOwner;
    private ViewModelStoreOwner viewModelStoreOwner;

    public SignUp1Add(Context context) {
        this.context = context;
    }

    public SignUp1Add(Context context, LifecycleOwner fragment, ViewModelStoreOwner viewModelStoreOwner) {
        this.context = context;
        this.lifecycleOwner = fragment;
        this.viewModelStoreOwner = viewModelStoreOwner;
    }

    public void requestingPhoneNumberHint(ActivityResultLauncher<IntentSenderRequest> retrievingThePhoneNumber){
        GetPhoneNumberHintIntentRequest request = GetPhoneNumberHintIntentRequest.builder().build();
        Identity.getSignInClient(context)
                .getPhoneNumberHintIntent(request)
                .addOnSuccessListener(result -> {
                    try {
                        IntentSender intentSender = result.getIntentSender();
                        retrievingThePhoneNumber.launch(new IntentSenderRequest.Builder(intentSender).build());
                    } catch (Exception e) {
                        Log.e("Error launching", "error occurred in launching Activity result.");
                    }
                })
                .addOnFailureListener(e -> Log.e("Failure occurred", "Failure getting phone number."));
    }

    public void requestUserByPhoneNumber(String phoneNumber){
        UserViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
        viewModel.getDetailsByPhone(phoneNumber.replace(" ", "").replace("-", "")).observe(lifecycleOwner, userEntity -> {
            try{
               if(userEntity != null){
                   context.startActivity(new Intent(context, SignUp2.class).putExtra("number", phoneNumber));
               } else  Toast.makeText(context, MakeToastEnum.NO_EXIST_PHONE_NUMBER.getError(), Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.e("TAG", "Ain't exist of this phone number in the database on the server.");
                Toast.makeText(context, MakeToastEnum.NO_CONNECTION_WITH_SERVER.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountryIsoCodeByPhoneNumber(String number) {
        if(!number.startsWith("+")){
            number = "+".concat(number);
        }
        if(parsingPhoneNumber(number) == null) {
            Log.i("TAG", "Don't exist ISO Alpha-2 code for this country code");
            return null;
        }
        else return PhoneNumberUtil.getInstance().getRegionCodeForCountryCode(Objects.requireNonNull(parsingPhoneNumber(number)).getCountryCode());
    }

    public String formattedNumber(String number){
        if(parsingPhoneNumber(number) == null) return null;
        return PhoneNumberUtil.getInstance().format(parsingPhoneNumber(number), PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
    }

    private Phonenumber.PhoneNumber parsingPhoneNumber(String number){
        try {
            return PhoneNumberUtil.getInstance().parse(number, null);
        } catch (NumberParseException e) {
            Log.e("TAG", "Phone number format doesn't exist in any country code");
            return null;
        }
    }
}
