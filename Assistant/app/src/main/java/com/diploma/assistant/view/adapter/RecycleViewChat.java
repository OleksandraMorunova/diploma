package com.diploma.assistant.view.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.entity.resource_service.Chat;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.service.firebase.PushNotificationSender;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_2.CheckStringLine;
import com.diploma.assistant.view_model.ChatViewModel;
import com.diploma.assistant.view_model.UserViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class RecycleViewChat extends RecyclerView.Adapter<RecycleViewChat.ViewHolderChat> implements Serializable{
    private final Activity activity;
    private final List<Chat> chat;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private final LifecycleOwner l;
    private final List<Integer> listPosition = new ArrayList<>();
    private final String id;

    private int count;
    private int updatePosition;
    private String bearerToken;
    private final String firebaseToken;

    private final List<User> userList = new ArrayList<>();

    public RecycleViewChat(Activity activity, List<Chat> chat, ViewModelStoreOwner viewModelStoreOwner, LifecycleOwner l, String firebaseToken, String id) {
        this.activity = activity;
        this.chat = chat;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.l = l;
        this.firebaseToken = firebaseToken;
        this.id = id;
    }

    @NonNull
    @Override
    public ViewHolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderChat(LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChat holder, int position) {
        holder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.primary, activity.getTheme()));
        AuthenticatorService accounts = new AuthenticatorService(activity);
        bearerToken = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode("uu74l8S6ewO/Nmrh3waPdCfyF7UFTUtFoI44Z5c75X0="));
        Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(bearerToken.replace("Bearer ", "")).getBody();
        List<String> roles = (List<String>) claims.get("role");
        if(Objects.equals(chat.get(position).getUserStatusMessage(), roles.get(0))){
            holder.relativeLayoutText.setGravity(Gravity.END);
            holder.message.setTextColor(activity.getResources().getColor(R.color.white, activity.getTheme()));
            holder.messageText.setTextColor(activity.getResources().getColor(R.color.white, activity.getTheme()));
        }
        else {
            holder.relativeLayoutText.setGravity(Gravity.START);
            holder.cardView.setBackgroundResource(R.color.background);
            holder.message.setTextColor(activity.getResources().getColor(R.color.background_text, activity.getTheme()));
            holder.messageText.setTextColor(activity.getResources().getColor(R.color.background_text, activity.getTheme()));
        }
        holder.messageText.setText(CheckStringLine.parserData(chat.get(position).getAddedData()));
        holder.message.setText(chat.get(position).getMessage());

        MaterialToolbar toolbar = activity.findViewById(R.id.toolbar_chat);
        toolbar.inflateMenu(R.menu.chat_menu);

        Menu menu = toolbar.getMenu();
        MenuItem itemDelete = menu.findItem(R.id.delete_message);
        MenuItem itemUpdate = menu.findItem(R.id.update_message);

        CardView cv = new CardView(activity);
        cv.setCardBackgroundColor(activity.getResources().getColor(R.color.yellow, activity.getTheme()));
        ColorStateList yellowColorStateList = ContextCompat.getColorStateList(activity.getApplicationContext(), R.color.yellow);
        ColorStateList whiteColorStateList = ContextCompat.getColorStateList(activity.getApplicationContext(), R.color.white);

        holder.itemView.setOnLongClickListener(v -> {
            if (chat.get(position).getUserStatusMessage().equals(roles.get(0))) {
                if (holder.cardView.getCardBackgroundColor().getDefaultColor() == activity.getResources().getColor(R.color.primary, activity.getTheme())) {
                    holder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.yellow, activity.getTheme()));
                    count++;
                    listPosition.add(position);
                    if(count == 1) {
                        String strPosition = String.valueOf(position);
                        updatePosition = Integer.parseInt(strPosition);
                    }
                } else {
                    holder.cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.primary, activity.getTheme()));
                    count--;
                    listPosition.remove((Integer) position);
                }
                itemUpdate.setVisible(count == 1);
                itemDelete.setVisible(count > 0);
            }
            return true;
        });

        ChatViewModel chatViewModel = new ViewModelProvider(viewModelStoreOwner).get(ChatViewModel.class);
        TextInputEditText editText = activity.findViewById(R.id.comment_chat_tiet);
        TextInputLayout inputEditText = activity.findViewById(R.id.comment_chat_til);

        inputEditText.setEndIconOnClickListener(o -> {
            if(itemUpdate.isVisible() && (itemUpdate.getIconTintList() == yellowColorStateList) && count == 1){
                String trimmedStr = Objects.requireNonNull(editText.getText()).toString().trim();
                if(!trimmedStr.isEmpty()) {
                    Chat newChat = new Chat();
                    newChat.setMessage(editText.getText().toString());
                    chatViewModel.updateChatMessage(bearerToken, chat.get(updatePosition).getId(), newChat).observe(l, d -> {
                        if(d != null && (updatePosition >= 0 && updatePosition < chat.size())){
                            listPosition.clear();
                            getMessage();
                            count = 0;
                            updatePosition = -1;
                            itemUpdate.setIconTintList(whiteColorStateList);
                            itemUpdate.setVisible(false);
                            editText.setText(null);
                        }
                    });
                } else Toast.makeText(activity.getApplicationContext(), "Пустий рядок", Toast.LENGTH_LONG).show();
                itemUpdate.setVisible(false);
            }
            else if(!itemUpdate.isVisible() && (itemUpdate.getIconTintList() != yellowColorStateList) && count == 0) {
                String trimmedStr = Objects.requireNonNull(editText.getText()).toString().trim();
                if(!trimmedStr.isEmpty()) {
                    String message = Objects.requireNonNull(editText.getText()).toString();
                    Chat chats = new Chat();
                    chats.setUserStatusMessage(getStatusFromToken(accounts).get(0));
                    chats.setUserId(id);
                    chats.setMessage(message);
                    chatViewModel.createChatMessage(bearerToken, chats).observe(l, e -> {
                       if(firebaseToken != null){
                          if(!firebaseToken.isEmpty()){
                              if (Build.VERSION.SDK_INT >= 33) {
                                  if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                                      PushNotificationSender sender = new PushNotificationSender();
                                      sender.sendNotification(firebaseToken, "Нове повідомлення з чату", message);
                                  }
                              } else {
                                  PushNotificationSender sender = new PushNotificationSender();
                                  sender.sendNotification(firebaseToken, "Нове повідомлення з чату", message);
                              }
                          }
                       } else {
                           UserViewModel userViewModel = new ViewModelProvider(viewModelStoreOwner).get(UserViewModel.class);
                           userViewModel.getUsers(bearerToken).observe(l, users -> {
                               if (users != null) {
                                   userList.addAll(users.getUserList());
                                   userList.removeIf(r -> !r.getRoles().contains("ADMIN"));

                                   for(User u: userList){
                                       if(! u.getUserTokenFirebase().isEmpty()){
                                           if (Build.VERSION.SDK_INT >= 33) {
                                               if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                                                   PushNotificationSender sender = new PushNotificationSender();
                                                   sender.sendNotification( u.getUserTokenFirebase(), "Нове повідомлення з чату", message);
                                               }
                                           } else {
                                               PushNotificationSender sender = new PushNotificationSender();
                                               sender.sendNotification( u.getUserTokenFirebase(), "Нове повідомлення з чату", message);
                                           }
                                       }
                                   }

                               }
                           });
                       }
                        getMessage();
                    });
                    editText.setText(null);
                    itemUpdate.setVisible(false);
                } else Toast.makeText(activity.getApplicationContext(), "Пустий рядок", Toast.LENGTH_LONG).show();
            }
        });

        itemUpdate.setOnMenuItemClickListener(k -> {
           if(itemUpdate.getIcon().isVisible()){
               InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
               itemDelete.setIconTintList(whiteColorStateList);
               itemUpdate.setIconTintList(yellowColorStateList);
           }
            return true;
        });

        itemDelete.setOnMenuItemClickListener(s -> {
             if (count > 0) {
                 listPosition.sort(Integer::compareTo);
                 List<Integer> positionList = new ArrayList<>(listPosition);
                 for (int i = positionList.size() - 1; i >= 0; i--) {
                     int index = positionList.get(i);
                     if(index < (chat).size()){
                         Chat message = chat.get(index);
                         chatViewModel.deleteChatMessageById(bearerToken, message.getId()).observe(l, d -> {
                             if (d.equals(true)) {
                                 chat.remove(message);
                                 notifyItemRemoved(index);
                             } else {
                                 Toast.makeText(activity.getApplicationContext(), "Щось пішло не так, спробуйте ще раз", Toast.LENGTH_SHORT).show();
                             }
                         });
                         listPosition.remove(i);
                     }
                 }
                 count = 0;
                 itemUpdate.setVisible(false);
                 itemDelete.setVisible(false);
                 notifyDataSetChanged();
             } else {
                 itemDelete.setIconTintList(whiteColorStateList);
             }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    private List<String> getStatusFromToken(AuthenticatorService accounts ){
        String bearerToken = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode("uu74l8S6ewO/Nmrh3waPdCfyF7UFTUtFoI44Z5c75X0="));
        Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(bearerToken.replace("Bearer ", "")).getBody();
        return  (List<String>) claims.get("role");
    }

    private void getMessage(){
        ChatViewModel viewModel = new ViewModelProvider(viewModelStoreOwner).get(ChatViewModel.class);
        viewModel.getAllMessageForChat(bearerToken, id).observe(l, g -> {
            if(g != null){
                chat.clear();
                chat.addAll(g);
                notifyDataSetChanged();
            }
        });
    }

    static class ViewHolderChat extends RecyclerView.ViewHolder {
        TextView message, messageText;
        RelativeLayout relativeLayoutText;
        CardView cardView;

        public ViewHolderChat(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view_text);
            message = itemView.findViewById(R.id.text_view_chat_item);
            messageText = itemView.findViewById(R.id.message_by_someone_and_datetime_text);
            relativeLayoutText = itemView.findViewById(R.id.relative_layout_text);
        }
    }
}