package com.diploma.assistant.view.ui.main_page.admin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.CommentsCertainBinding;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfComments;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfCommentsCertain;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.entity.resource_service.CommentsDto;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.service.firebase.PushNotificationSender;
import com.diploma.assistant.view.adapter.RecycleViewCommentsCertainContext;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_2.CheckStringLine;
import com.diploma.assistant.view_model.CommentsViewModel;
import com.diploma.assistant.view_model.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CertainComment extends AppCompatActivity{
    private final List<ItemsForListOfCommentsCertain> viewListOfComments = new ArrayList<>();
    private CommentsCertainBinding binding;
    private String token, firebaseToken;

    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CommentsCertainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AuthenticatorService service = new AuthenticatorService(this);
        String id = service.getStringFromSharedPreferences("id_user", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
        AuthenticatorService accounts = new AuthenticatorService(this);
        token = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");

        String idTask = getIntent().getStringExtra("id_task");
        String commentUser = getIntent().getStringExtra("comment_user");
        String commentData = getIntent().getStringExtra("comment_data");
        String article = getIntent().getStringExtra("article");
        String comment = getIntent().getStringExtra("comment");
        firebaseToken = getIntent().getStringExtra("firebase_token");
        List<ItemsForListOfComments> list = (List<ItemsForListOfComments>) getIntent().getSerializableExtra("list_of_comments");

        TextView commentNameContainer = findViewById(R.id.certain_user_name_comment);
        TextView commentDataContainer = findViewById(R.id.certain_data_time_comment);
        TextView commentContainer = findViewById(R.id.certain_text_of_comment);
        TextView commentArticle = findViewById(R.id.certain_task_name_comment);

        commentNameContainer.setText(commentUser);
        commentDataContainer.setText(CheckStringLine.parserData(commentData));
        commentContainer.setText(comment);
        commentArticle.setText(article);

        RecyclerView navRecyclerView = findViewById(R.id.recycle_view_certain_text_of_comment);
        for(ItemsForListOfComments commentsDto: list){
            viewListOfComments.add(new ItemsForListOfCommentsCertain(CheckStringLine.parserData(commentsDto.getComment_added_data()), commentsDto.getComment()));
        }

        navRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        navRecyclerView.setAdapter(new RecycleViewCommentsCertainContext(this, viewListOfComments));

       if(id != null){
           binding.commentTextInputLayout.setEndIconOnClickListener(v -> {
               String message = Objects.requireNonNull(binding.commentTextInputEditText.getText()).toString();
               CommentsViewModel viewModel = new ViewModelProvider(this).get(CommentsViewModel.class);
               CommentsDto dto = new CommentsDto();
               dto.setUser_comment_id(id);
               dto.setComment(message);
               viewModel.postComment(token, idTask, dto).observe(this, p -> {
                   if(p != null){
                      if(firebaseToken != null){
                          if (Build.VERSION.SDK_INT >= 33) {
                              if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                                  PushNotificationSender sender = new PushNotificationSender();
                                  sender.sendNotification(firebaseToken, "Вам надіслали новий коментар", message);
                              }
                          } else {
                              PushNotificationSender sender = new PushNotificationSender();
                              sender.sendNotification(firebaseToken, "Вам надіслали новий коментар", message);
                          }
                      } else {
                          UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
                          userViewModel.getUsers(token).observe(this, users -> {
                              if (users != null) {
                                  userList.addAll(users.getUserList());
                                  userList.removeIf(r -> !r.getRoles().contains("ADMIN"));
                                  for(User u: userList){
                                      if(! u.getUserTokenFirebase().isEmpty()){
                                          if (Build.VERSION.SDK_INT >= 33) {
                                              if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
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
                       viewListOfComments.add(new ItemsForListOfCommentsCertain(CheckStringLine.parserData(p.getAddedData()), binding.commentTextInputEditText.getText().toString()));
                       navRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                       navRecyclerView.setAdapter(new RecycleViewCommentsCertainContext(this, viewListOfComments));
                       binding.commentTextInputEditText.setText(null);
                   }
               });
           });
       }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}