package com.diploma.assistant.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfCommentsCertain;

import java.io.Serializable;
import java.util.List;

public class RecycleViewCommentsCertainContext extends RecyclerView.Adapter<ViewHolderCommentsContextCertain> implements Serializable{
    private final Context context;
    private final List<ItemsForListOfCommentsCertain> items;

    public RecycleViewCommentsCertainContext(Context context, List<ItemsForListOfCommentsCertain> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolderCommentsContextCertain onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderCommentsContextCertain(LayoutInflater.from(context).inflate(R.layout.comments_certain_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCommentsContextCertain holder, int position) {
        holder.comments.setText(items.get(position).getComment());
        holder.commentsData.setText(items.get(position).getComment_added_data());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
