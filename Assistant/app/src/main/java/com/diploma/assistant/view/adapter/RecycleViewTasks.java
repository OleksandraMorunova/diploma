package com.diploma.assistant.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.model.entity.resource_service.TaskDto;
import com.diploma.assistant.view.ui.tasks.TaskActivity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecycleViewTasks extends RecyclerView.Adapter<ViewHolderTasks> implements Serializable, Filterable {
    private final Context context;
    private final List<TaskDto> items;
    private final List<TaskDto> itemsFull;

    public RecycleViewTasks(Context context, List<TaskDto> items) {
        this.context = context;
        this.items = items;
        itemsFull = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public ViewHolderTasks onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTasks(LayoutInflater.from(context).inflate(R.layout.tasks_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTasks holder, int position) {
        String time = LocalDateTime.parse(items.get(position).getAddedData()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + " by " + items.get(position).getUserId();
        holder.datatime.setText(time);
        holder.article.setText(items.get(position).getTitle());
        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, TaskActivity.class));
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
}
