package com.diploma.assistant.view.adapter;

import android.content.Context;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfCommentsCertain;
import com.diploma.assistant.view_model.CommentsViewModel;

import java.io.Serializable;
import java.util.List;

public class RecycleViewCommentsCertainContext extends RecyclerView.Adapter<RecycleViewCommentsCertainContext.ViewHolderCommentsContextCertain> implements Serializable{
    private final Context context;
    private final List<ItemsForListOfCommentsCertain> items;
    private List<String> user;
    private String id;

    public RecycleViewCommentsCertainContext(Context context, List<ItemsForListOfCommentsCertain> items) {
        this.context = context;
        this.items = items;
    }

    public RecycleViewCommentsCertainContext(Context context, List<ItemsForListOfCommentsCertain> items, List<String> user, String id) {
        this.context = context;
        this.items = items;
        this.user = user;
        this.id = id;
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

        if(user != null && user.get(position).equals(id)){
            holder.comments.setGravity(Gravity.END);
            holder.commentsData.setGravity(Gravity.END);
        } else {
            holder.comments.setGravity(Gravity.START);
            holder.commentsData.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolderCommentsContextCertain extends RecyclerView.ViewHolder {
        TextView comments, commentsData;

        public ViewHolderCommentsContextCertain(@NonNull View itemView) {
            super(itemView);
            comments = itemView.findViewById(R.id.tex_of_comment_item_comments_certain);
            commentsData = itemView.findViewById(R.id.datatime_item_comments_certain);
        }
    }
}