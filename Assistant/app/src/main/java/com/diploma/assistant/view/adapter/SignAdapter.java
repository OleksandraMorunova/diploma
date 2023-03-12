package com.diploma.assistant.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.*;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.diploma.assistant.view.ui.fragment.auth.sign_in.SignInFragment;
import com.diploma.assistant.view.ui.fragment.auth.sign_up.*;

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
        return new SignUp1();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
