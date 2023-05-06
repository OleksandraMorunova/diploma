package com.diploma.assistant.view.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;

public class ViewHolderTasks extends RecyclerView.ViewHolder {
    TextView datatime, article;

    public ViewHolderTasks(@NonNull View itemView) {
        super(itemView);
        datatime = itemView.findViewById(R.id.datatime_and_owner_view_task);
        article = itemView.findViewById(R.id.article_task);
    }
}
