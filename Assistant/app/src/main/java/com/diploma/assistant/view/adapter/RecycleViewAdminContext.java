package com.diploma.assistant.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.model.entity.adapter.ItemsForListOfUsers;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.view.ui.tasks.ChatFragment;
import com.diploma.assistant.view.ui.tasks.TasksListActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecycleViewAdminContext extends RecyclerView.Adapter<ViewHolderAdminContext> implements Filterable, Serializable {
    private final Context context;
    private final List<ItemsForListOfUsers> items;
    private final List<ItemsForListOfUsers> itemsFull;

    public RecycleViewAdminContext(Context context, List<ItemsForListOfUsers> items) {
        this.context = context;
        this.items = items;
        itemsFull = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public ViewHolderAdminContext onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAdminContext(LayoutInflater.from(context).inflate(R.layout.user_admin_item, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdminContext holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.countView.setText(items.get(position).getCount());
        holder.statusView.setText(items.get(position).getStatus());
        holder.itemView.setOnClickListener(v -> {
            Context wrapper = new ContextThemeWrapper(v.getContext(), R.style.PopMenu);
            PopupMenu popup = new PopupMenu(wrapper, v);
            popup.setForceShowIcon(true);
            popup.inflate(R.menu.menu_items_day);
            popup.setGravity(Gravity.CENTER);
            popup.setOnMenuItemClickListener(item -> {
                final int itemId = item.getItemId();
                if (itemId == R.id.option_1) {
                    context.startActivity(new Intent(context, TasksListActivity.class)
                            .putExtra("id", items.get(position).getId())
                            .putExtra("name", items.get(position).getName())
                            .putExtra("count", items.get(position).getCount())
                            .putExtra("icon", (Parcelable) items.get(position).getIcon())
                    );
                    return true;
                }
                if (itemId == R.id.option_2) {
                    context.startActivity(new Intent(context, ChatFragment.class));
                    return true;
                }
                return itemId == R.id.option_3;
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
}
