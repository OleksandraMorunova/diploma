package com.diploma.assistant.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfComments;
import com.diploma.assistant.view.ui.navigation.CertainComment;
import com.diploma.assistant.view_model.CommentsViewModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RecycleViewCommentsContext extends RecyclerView.Adapter<ViewHolderCommentsContext> implements Serializable{
    private final Context context;
    private final List<ItemsForListOfComments> items;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private final LifecycleOwner lifecycle;

    public RecycleViewCommentsContext(Context context, List<ItemsForListOfComments> items, ViewModelStoreOwner viewModelStoreOwner, LifecycleOwner lifecycle) {
        this.context = context;
        this.items = items;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.lifecycle = lifecycle;
    }

    @NonNull
    @Override
    public ViewHolderCommentsContext onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderCommentsContext(LayoutInflater.from(context).inflate(R.layout.comments_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCommentsContext holder, int position) {
        holder.comments.setText(items.get(position).getComment());
        holder.commentsData.setText(LocalDateTime.parse(items.get(position).getComment_added_data()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        holder.userName.setText(items.get(position).getUserName());
        holder.article.setText(items.get(position).getArticle());

        holder.itemView.setOnClickListener(v -> {
            List<ItemsForListOfComments> it = new ArrayList<>(items);
            Predicate<ItemsForListOfComments> predicate =  p->!p.getArticleId().equals(it.get(position).getArticleId()) || LocalDateTime.parse(p.getComment_added_data()).compareTo(LocalDateTime.parse(items.get(position).getComment_added_data())) <= 0;
            it.removeIf(predicate);

            if(items.get(position).getReviewed().equals(false)){
                CommentsViewModel commentsViewModel = new ViewModelProvider(viewModelStoreOwner).get(CommentsViewModel.class);
                commentsViewModel.updateComment(items.get(position).getArticleId(), items.get(position).getIdCommentsByExistList()).observe(lifecycle, l -> {});
            }

            context.startActivity(new Intent(context, CertainComment.class)
                    .putExtra("comment", items.get(position).getComment())
                    .putExtra("comment_data", items.get(position).getComment_added_data())
                    .putExtra("comment_user", items.get(position).getUserName())
                    .putExtra("article", items.get(position).getArticle())
                    .putExtra("list_of_comments", (Serializable) it)
            );
        });

        if(items.get(position).getReviewed()){
            holder.comments.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.item_comments_normal, null));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
