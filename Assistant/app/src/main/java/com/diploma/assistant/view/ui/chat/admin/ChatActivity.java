package com.diploma.assistant.view.ui.chat.admin;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.ChatFinallyBinding;
import com.diploma.assistant.view.ui.chat.ChatRealization;

public class ChatActivity extends AppCompatActivity {
    private ChatFinallyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChatFinallyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String id = getIntent().getStringExtra("user_id");
        String firebaseToken = getIntent() .getStringExtra("firebase_token");

        final View activityRootView = findViewById(R.id.view_chat_coordinator);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            ViewGroup.LayoutParams layoutParams = binding.recycleViewChat.getLayoutParams();
            Rect r = new Rect();
            activityRootView.getWindowVisibleDisplayFrame(r);
            int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
            boolean isKeyboardVisible = heightDiff > dpToPx(this);
            layoutParams.height = isKeyboardVisible ? dpToPx(this) : ViewGroup.LayoutParams.MATCH_PARENT;
            binding.recycleViewChat.setLayoutParams(layoutParams);
        });

        ChatRealization realization = new ChatRealization(this, binding, this, this);
        RecyclerView recyclerView1 = findViewById(R.id.recycle_view_chat);
        realization.mainAction(id, firebaseToken, recyclerView1);
        SwipeRefreshLayout swipe = findViewById(R.id.swipe_chat);

        swipe.setOnRefreshListener(() -> {
            realization.getMessage();
            swipe.setRefreshing(false);
        });
    }

    public int dpToPx(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) 500 * density);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}