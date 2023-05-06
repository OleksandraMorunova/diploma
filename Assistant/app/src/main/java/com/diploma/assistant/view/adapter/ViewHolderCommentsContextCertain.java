package com.diploma.assistant.view.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;

public class ViewHolderCommentsContextCertain extends RecyclerView.ViewHolder {
    TextView comments, commentsData;

    public ViewHolderCommentsContextCertain(@NonNull View itemView) {
        super(itemView);
        comments = itemView.findViewById(R.id.tex_of_comment_item_comments_certain);
        commentsData = itemView.findViewById(R.id.datatime_item_comments_certain);
    }
}
