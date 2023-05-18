package com.diploma.assistant.view.ui.chat;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.ChatFinallyBinding;
import com.diploma.assistant.model.entity.resource_service.Chat;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.adapter.RecycleViewChat;
import com.diploma.assistant.view_model.ChatViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class ChatActivity extends AppCompatActivity {
    private ChatFinallyBinding binding;
    private RecycleViewChat mRAdapter;
    private final List<Chat> chatList = new ArrayList<>();

    private String id, bearerToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChatFinallyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        id = getIntent().getStringExtra("user_id");
        AuthenticatorService accounts = new AuthenticatorService(this);
        bearerToken = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");

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

        getMessage();
        SwipeRefreshLayout swipe = findViewById(R.id.swipe_chat);

        swipe.setOnRefreshListener(() -> {
          getMessage();
          swipe.setRefreshing(false);
        });

        binding.commentChatTil.setEndIconOnClickListener(o -> {
            ChatViewModel chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
                Chat chats = new Chat();
                chats.setUserStatusMessage(getStatusFromToken(accounts).get(0));
                chats.setUserId(id);
                chats.setMessage(Objects.requireNonNull(binding.commentChatTiet.getText()).toString());
                chatViewModel.createChatMessage(bearerToken, chats).observe(this, e -> {
                    if(e != null){
                        for(int j = 0; j < 1; j++){
                            chatList.add(e);
                            mRAdapter.notifyDataSetChanged();
                        }
                    }
                });
               binding.commentChatTiet.setText(null);
        });

        ChatViewModel viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        viewModel.getAllMessageForChat(bearerToken, id).observe(this, g -> {
            chatList.clear();
           if(g != null){
               chatList.addAll(g);
           }
        });
        RecyclerView recyclerView1 = findViewById(R.id.recycle_view_chat);
        mRAdapter = new RecycleViewChat(this, chatList, this, this, id);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(mRAdapter);
    }

    private void getMessage(){
        ChatViewModel viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        viewModel.getAllMessageForChat(bearerToken, id).observe(this, g -> {
            if(g != null){
                chatList.clear();
                chatList.addAll(g);
                mRAdapter.notifyDataSetChanged();
            }
        });
    }

    private List<String> getStatusFromToken(AuthenticatorService accounts ){
        String bearerToken = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode("uu74l8S6ewO/Nmrh3waPdCfyF7UFTUtFoI44Z5c75X0="));
        Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(bearerToken.replace("Bearer ", "")).getBody();
        return  (List<String>) claims.get("role");
    }

    private int dpToPx(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) 500 * density);
    }
}