package com.diploma.assistant.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.*;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.diploma.assistant.view.ui.sign_in.fragment.SignInFragment;
import com.diploma.assistant.view.ui.sign_up.fragment.SignUpInputPhoneNumberFragment;

public class SignAdapter extends FragmentStateAdapter {

    public SignAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return new SignInFragment();
        }
        return new SignUpInputPhoneNumberFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
