package com.diploma.assistant.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;

public class ViewHolderAdminContext extends RecyclerView.ViewHolder {
    TextView nameView, statusView, countView;

    public ViewHolderAdminContext(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.name_view);
        statusView = itemView.findViewById(R.id.status_user_view);
        countView = itemView.findViewById(R.id.count_tasks_view);
    }

}
