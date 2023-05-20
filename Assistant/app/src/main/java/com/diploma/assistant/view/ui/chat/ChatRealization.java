package com.diploma.assistant.view.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
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

public class ChatRealization {
    private RecycleViewChat mRAdapter;
    private final List<Chat> chatList = new ArrayList<>();
    private String id, bearerToken, firebaseToken;

    private final Activity activity;
    private final ChatFinallyBinding binding;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private final LifecycleOwner lifecycleOwner;

    public ChatRealization(Activity activity, ChatFinallyBinding binding, ViewModelStoreOwner viewModelStoreOwner, LifecycleOwner lifecycleOwner) {
        this.activity = activity;
        this.binding = binding;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.lifecycleOwner = lifecycleOwner;
    }

    public void mainAction(String id, String firebaseToken, RecyclerView recyclerView){
        this.id = id;
        this.firebaseToken = firebaseToken;

        AuthenticatorService accounts = new AuthenticatorService(activity);
        bearerToken = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        getMessage();

        binding.commentChatTil.setEndIconOnClickListener(o -> {
            ChatViewModel chatViewModel = new ViewModelProvider(viewModelStoreOwner).get(ChatViewModel.class);
            Chat chats = new Chat();
            chats.setUserStatusMessage(getStatusFromToken(accounts).get(0));
            chats.setUserId(id);
            chats.setMessage(Objects.requireNonNull(binding.commentChatTiet.getText()).toString());
            chatViewModel.createChatMessage(bearerToken, chats).observe(lifecycleOwner, e -> {
                if(e != null){
                    for(int j = 0; j < 1; j++){
                        chatList.add(e);
                        mRAdapter.notifyDataSetChanged();
                    }
                }
            });
            binding.commentChatTiet.setText(null);
        });

        mRAdapter = new RecycleViewChat(activity, chatList,  viewModelStoreOwner, lifecycleOwner, firebaseToken, id);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext()));
        recyclerView.setAdapter(mRAdapter);
    }

    public void getMessage(){
        ChatViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(ChatViewModel.class);
        viewModel.getAllMessageForChat(bearerToken, id).observe(lifecycleOwner, g -> {
            if(g != null){
                chatList.clear();
                chatList.addAll(g);
                mRAdapter.notifyDataSetChanged();
            }
        });
    }

    public List<String> getStatusFromToken(AuthenticatorService accounts ){
        String bearerToken = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode("uu74l8S6ewO/Nmrh3waPdCfyF7UFTUtFoI44Z5c75X0="));
        Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(bearerToken.replace("Bearer ", "")).getBody();
        return  (List<String>) claims.get("role");
    }
}
