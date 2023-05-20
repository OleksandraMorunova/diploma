package com.diploma.assistant.view.ui.chat.user;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.ChatFinallyBinding;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.ui.chat.ChatRealization;

public class ChatUserFragment extends Fragment {
    private ChatFinallyBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ChatFinallyBinding.inflate(inflater, container, false);

        AuthenticatorService accounts = new AuthenticatorService(getActivity());
        String firebaseToken =  accounts.getStringFromSharedPreferences("firebase_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        String id = accounts.getStringFromSharedPreferences("id_user", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        ChatRealization realization = new ChatRealization(getActivity(), binding, this, this);
        RecyclerView recyclerView1 = binding.recycleViewChat;
        realization.mainAction(id, firebaseToken, recyclerView1);
        SwipeRefreshLayout swipe = binding.swipeChat;
        swipe.setOnRefreshListener(() -> {
            realization.getMessage();
            swipe.setRefreshing(false);
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
