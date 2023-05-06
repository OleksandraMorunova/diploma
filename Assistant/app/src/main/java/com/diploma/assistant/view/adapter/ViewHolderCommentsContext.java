package com.diploma.assistant.view.adapter;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;

public class ViewHolderCommentsContext extends RecyclerView.ViewHolder {
    TextView comments, commentsData, userName, article;

    public ViewHolderCommentsContext(@NonNull View itemView) {
        super(itemView);
        comments = itemView.findViewById(R.id.nav_comment_view);
        commentsData = itemView.findViewById(R.id.nav_data_time_comment_view);
        userName = itemView.findViewById(R.id.nav_user_name_comment_view);
        article = itemView.findViewById(R.id.nav_article);
    }
}
