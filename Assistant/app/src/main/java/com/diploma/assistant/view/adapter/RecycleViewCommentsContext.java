package com.diploma.assistant.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfComments;
import com.diploma.assistant.model.enumaration.StatusUserEnum;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.ui.main_page.admin.CertainComment;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_2.CheckStringLine;
import com.diploma.assistant.view_model.CommentsViewModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class RecycleViewCommentsContext extends RecyclerView.Adapter<RecycleViewCommentsContext.ViewHolderCommentsContext> implements Serializable{
    private final Activity activity;
    private final List<ItemsForListOfComments> items;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private final LifecycleOwner lifecycle;

    public RecycleViewCommentsContext(Activity activity, List<ItemsForListOfComments> items, ViewModelStoreOwner viewModelStoreOwner, LifecycleOwner lifecycle) {
        this.activity = activity;
        this.items = items;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.lifecycle = lifecycle;
    }

    @NonNull
    @Override
    public ViewHolderCommentsContext onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderCommentsContext(LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.comments_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCommentsContext holder, int position) {
        holder.comments.setText(items.get(position).getComment());
        holder.commentsData.setText(CheckStringLine.parserData(items.get(position).getComment_added_data()));
        holder.userName.setText(items.get(position).getUserName());
        holder.article.setText(items.get(position).getArticle());

        holder.itemView.setOnClickListener(v -> {
            AuthenticatorService accounts = new AuthenticatorService(activity);
            String bearerToken = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");
            String status = accounts.getTokenProperty(bearerToken).getBody().get("status", String.class);
            if(Objects.equals(status, StatusUserEnum.ACTIVE.getStatus())){
                List<ItemsForListOfComments> it = new ArrayList<>(items);
                Predicate<ItemsForListOfComments> predicate =  p->!p.getArticleId().equals(it.get(position).getArticleId()) || LocalDateTime.parse(p.getComment_added_data()).compareTo(LocalDateTime.parse(items.get(position).getComment_added_data())) <= 0;
                it.removeIf(predicate);

                if(items.get(position).getReviewed().equals(false)){
                   CommentsViewModel commentsViewModel = new ViewModelProvider(viewModelStoreOwner).get(CommentsViewModel.class);
                    commentsViewModel.updateComment(bearerToken, items.get(position).getArticleId(), items.get(position).getIdCommentsByExistList()).observe(lifecycle, l -> {});
                }

                activity.startActivity(new Intent(activity.getApplicationContext(), CertainComment.class)
                        .putExtra("id_task", items.get(position).getIdTask())
                        .putExtra("comment", items.get(position).getComment())
                        .putExtra("comment_data", items.get(position).getComment_added_data())
                        .putExtra("comment_user", items.get(position).getUserName())
                        .putExtra("article", items.get(position).getArticle())
                        .putExtra("list_of_comments", (Serializable) it)
                        .putExtra("firebase_token", items.get(position).getFirebaseToken())
                );

            }
        });

        if(items.get(position).getReviewed()){
            holder.comments.setTextColor(ResourcesCompat.getColor(activity.getResources(), R.color.item_comments_normal, null));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolderCommentsContext extends RecyclerView.ViewHolder {
        TextView comments, commentsData, userName, article;

        public ViewHolderCommentsContext(@NonNull View itemView) {
            super(itemView);
            comments = itemView.findViewById(R.id.nav_comment_view);
            commentsData = itemView.findViewById(R.id.nav_data_time_comment_view);
            userName = itemView.findViewById(R.id.nav_user_name_comment_view);
            article = itemView.findViewById(R.id.nav_article);
        }
    }
}
