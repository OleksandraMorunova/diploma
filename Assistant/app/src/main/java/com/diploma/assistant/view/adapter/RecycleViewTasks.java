package com.diploma.assistant.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.model.enumaration.TypeUserEnum;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.ui.sign_up.activity.sign_up_2.CheckStringLine;
import com.diploma.assistant.view.ui.tasks.admin.TaskActivity;
import com.diploma.assistant.view.ui.tasks.user.TaskFragment;
import com.diploma.assistant.view_model.TasksViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class RecycleViewTasks extends RecyclerView.Adapter<RecycleViewTasks.ViewHolderTasks> implements Serializable, Filterable {
    private final Activity activity;
    private final List<TaskDto> items;
    private final List<TaskDto> itemsFull;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private final LifecycleOwner lifecycle;
    private String token;
    private final String name;

    public RecycleViewTasks(Activity activity, List<TaskDto> items, String id, ViewModelStoreOwner v, LifecycleOwner lifecycle, String name) {
        this.activity = activity;
        this.items = items;
        this.viewModelStoreOwner = v;
        this.lifecycle = lifecycle;
        this.name = name;
        itemsFull = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public ViewHolderTasks onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTasks(LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.tasks_list_item, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderTasks holder, int position) {
        String time = CheckStringLine.parserData(items.get(position).getAddedData());
        holder.datetime.setText(time);
        holder.article.setText(items.get(position).getTitle());
        AuthenticatorService accounts = new AuthenticatorService(activity);
        token = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");

        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode("uu74l8S6ewO/Nmrh3waPdCfyF7UFTUtFoI44Z5c75X0="));
        Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token.replace("Bearer ", "")).getBody();
        List<String> roles = (List<String>) claims.get("role");

        if(roles.get(0).equals(TypeUserEnum.ADMIN.getTypeUserName())) {
            holder.itemView.setOnLongClickListener(v -> {
                Context wrapper = new ContextThemeWrapper(v.getContext(), R.style.PopMenu);
                PopupMenu popup = new PopupMenu(wrapper, v);
                popup.setForceShowIcon(true);
                popup.inflate(R.menu.tasks_current_menu);
                popup.setGravity(Gravity.CENTER);

                popup.setOnMenuItemClickListener(item -> {
                    final int itemId = item.getItemId();
                    if (itemId == R.id.delete_tasks) {
                        TasksViewModel tasksViewModel = new ViewModelProvider(viewModelStoreOwner).get(TasksViewModel.class);
                        tasksViewModel.deleteTask(token, items.get(position).getId()).observe(lifecycle, l -> {
                            if (l.equals(true)) {
                                items.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, items.size());
                                Toast.makeText(activity.getApplicationContext(), "Завдання успішно видалене!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(activity.getApplicationContext(), "Щось пішло не так, спробуйте будь-ласка ще раз", Toast.LENGTH_SHORT).show();
                        });
                    }
                    return false;
                });

                popup.show();
                return false;
            });

            holder.itemView.setOnClickListener(v -> {
                activity.startActivity(new Intent(activity.getApplicationContext(), TaskActivity.class)
                        .putExtra("id", items.get(position).getId())
                        .putExtra("name", name));
            });
        } else {
            holder.itemView.setOnClickListener(v -> {
                activity.startActivity(new Intent(activity.getApplicationContext(), TaskFragment.class)
                        .putExtra("id", items.get(position).getId())
                        .putExtra("name", name));
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TaskDto> filterItems = new ArrayList<>();
            if(constraint.length() == 0){
                filterItems.addAll(itemsFull);
            } else {
                String pattern = constraint.toString().toLowerCase(Locale.ROOT).trim();
                for(TaskDto t: itemsFull){
                    if(t.getTitle().toLowerCase(Locale.ROOT).contains(pattern)){
                        filterItems.add(t);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterItems;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class ViewHolderTasks extends RecyclerView.ViewHolder {
        TextView datetime, article;

        public ViewHolderTasks(@NonNull View itemView) {
            super(itemView);
            datetime = itemView.findViewById(R.id.datatime_and_owner_view_task);
            article = itemView.findViewById(R.id.article_task);
        }
    }
}