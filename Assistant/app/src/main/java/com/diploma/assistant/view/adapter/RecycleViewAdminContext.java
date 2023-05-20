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
import com.diploma.assistant.model.entity.adapter.ItemsForListOfUsers;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.model.enumaration.StatusUserEnum;
import com.diploma.assistant.service.account_manager.AuthenticatorService;
import com.diploma.assistant.view.ui.chat.admin.ChatActivity;
import com.diploma.assistant.view.ui.tasks.admin.TasksListActivity;
import com.diploma.assistant.view_model.UserViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RecycleViewAdminContext extends RecyclerView.Adapter< RecycleViewAdminContext.ViewHolderAdminContext> implements Filterable, Serializable {
    private final Activity context;
    private final List<ItemsForListOfUsers> items;
    private final List<ItemsForListOfUsers> itemsFull;

    private final ViewModelStoreOwner vmso;
    private final LifecycleOwner lo;

    public RecycleViewAdminContext(Activity context, List<ItemsForListOfUsers> items, ViewModelStoreOwner vmso, LifecycleOwner lo) {
        this.context = context;
        this.items = items;
        itemsFull = new ArrayList<>(items);
        this.vmso = vmso;
        this.lo = lo;
    }

    @NonNull
    @Override
    public ViewHolderAdminContext onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAdminContext(LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.user_admin_item, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdminContext holder, int position) {
        AuthenticatorService accounts = new AuthenticatorService(context);
        String token = accounts.getElementFromSet("Bearer", "jwt_token", "com.assistant.emmotechie.PREFERENCE_FILE_KEY");

        holder.nameView.setText(items.get(position).getName());
        holder.countView.setText(items.get(position).getCount());
        holder.statusView.setText(items.get(position).getStatus());

        holder.itemView.setOnClickListener(v -> {
            Context wrapper = new ContextThemeWrapper(v.getContext(), R.style.PopMenu);
            PopupMenu popup = new PopupMenu(wrapper, v);
            popup.setForceShowIcon(true);
            popup.inflate(R.menu.menu_items_day);
            popup.setGravity(Gravity.CENTER);

           String status = accounts.getTokenProperty(token).getBody().get("status", String.class);

            UserViewModel userViewModel = new ViewModelProvider(vmso).get(UserViewModel.class);
            User user = new User();
            popup.setOnMenuItemClickListener(item -> {
                final int itemId = item.getItemId();
                if (itemId == R.id.option_1) {
                    if(Objects.equals(status, StatusUserEnum.ACTIVE.getStatus())){
                        context.startActivity(new Intent(context, TasksListActivity.class)
                                .putExtra("id", items.get(position).getId())
                                .putExtra("name", items.get(position).getName())
                                .putExtra("count", items.get(position).getCount())
                                .putExtra("icon", items.get(position).getIcon())
                                .putExtra("firebase_token", items.get(position).getFirebaseToken())
                        );
                    }
                    return true;
                }
                if (itemId == R.id.option_2) {
                    if(Objects.equals(status, StatusUserEnum.ACTIVE.getStatus())) {
                        context.startActivity(new Intent(context, ChatActivity.class)
                                .putExtra("user_id", items.get(position).getId())
                                .putExtra("firebase_token", items.get(position).getFirebaseToken()));
                    }
                    return true;
                }
                if (itemId == R.id.option_3_1) {
                    if(Objects.equals(status, StatusUserEnum.ACTIVE.getStatus())) {
                        user.setStatus(StatusUserEnum.BLOCKED.getStatus());
                        userViewModel.updateUserDetails(token, items.get(position).getId(), user, null).observe(lo, l -> {
                            if (l != null) {
                                holder.statusView.setText(StatusUserEnum.BLOCKED.getStatus());
                                Toast.makeText(context.getApplicationContext(), "Дані успішно оновленно", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(context.getApplicationContext(), "Щось пішло не так", Toast.LENGTH_SHORT).show();
                        });
                    }
                    return true;
                }
                if (itemId == R.id.option_3_2){
                    if(Objects.equals(status, StatusUserEnum.ACTIVE.getStatus())) {
                        userViewModel.deleteUserById(token, items.get(position).getId()).observe(lo, l -> {
                            if (l) {
                                Toast.makeText(context.getApplicationContext(), "Успішно видаленно", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(context.getApplicationContext(), "Щось пішло не так", Toast.LENGTH_SHORT).show();
                        });
                    }
                    return true;
                }

                if (itemId == R.id.option_3_3){
                    if(Objects.equals(status, StatusUserEnum.ACTIVE.getStatus())) {
                        user.setStatus(StatusUserEnum.ACTIVE.getStatus());
                        userViewModel.updateUserDetails(token, items.get(position).getId(), user, null).observe(lo, l -> {
                            if (l != null) {
                                holder.statusView.setText(StatusUserEnum.ACTIVE.getStatus());
                                Toast.makeText(context.getApplicationContext(), "Дані успішно оновленно", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(context.getApplicationContext(), "Щось пішло не так", Toast.LENGTH_SHORT).show();
                        });
                    }
                    return true;
                }
                return false;
            });
            popup.show();
        });
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
            List<ItemsForListOfUsers> filterItems = new ArrayList<>();
            if(constraint.length() == 0){
                filterItems.addAll(itemsFull);
            } else {
                String pattern = constraint.toString().toLowerCase(Locale.ROOT).trim();
                for(ItemsForListOfUsers i: itemsFull){
                    if(i.getName().toLowerCase(Locale.ROOT).contains(pattern) || i.getCount().toLowerCase(Locale.ROOT).contains(pattern)){
                        filterItems.add(i);
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

    static class ViewHolderAdminContext extends RecyclerView.ViewHolder {
        TextView nameView, statusView, countView;

        public ViewHolderAdminContext(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name_view);
            statusView = itemView.findViewById(R.id.status_user_view);
            countView = itemView.findViewById(R.id.count_tasks_view);
        }
    }
}
