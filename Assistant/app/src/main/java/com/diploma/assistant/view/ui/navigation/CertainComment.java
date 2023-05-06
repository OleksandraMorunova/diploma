package com.diploma.assistant.view.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.databinding.CommentsCertainBinding;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfComments;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfCommentsCertain;
import com.diploma.assistant.model.entity.resource_service.CommentsDto;
import com.diploma.assistant.view.adapter.RecycleViewCommentsCertainContext;
import com.diploma.assistant.view.adapter.RecycleViewCommentsContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CertainComment extends AppCompatActivity {
    List<ItemsForListOfCommentsCertain> viewListOfComments = new ArrayList<>();

    private CommentsCertainBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CommentsCertainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String commentUser = getIntent().getStringExtra("comment_user");
        String commentData = getIntent().getStringExtra("comment_data");
        String article = getIntent().getStringExtra("article");
        String comment = getIntent().getStringExtra("comment");
        List<ItemsForListOfComments> list = (List<ItemsForListOfComments>) getIntent().getSerializableExtra("list_of_comments");

        RecyclerView navRecyclerView = findViewById(R.id.recycle_view_certain_text_of_comment);
        for(ItemsForListOfComments commentsDto: list){
            viewListOfComments.add(new ItemsForListOfCommentsCertain(LocalDateTime.parse(commentsDto.getComment_added_data()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), commentsDto.getComment()));
        }

        TextView commentNameContainer = findViewById(R.id.certain_user_name_comment);
        TextView commentDataContainer = findViewById(R.id.certain_data_time_comment);
        TextView commentContainer = findViewById(R.id.certain_text_of_comment);
        TextView commentArticle = findViewById(R.id.certain_task_name_comment);

        commentNameContainer.setText(commentUser);
        commentDataContainer.setText(LocalDateTime.parse(commentData).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        commentContainer.setText(comment);
        commentArticle.setText(article);

        navRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        navRecyclerView.setAdapter(new RecycleViewCommentsCertainContext(this, viewListOfComments));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}
