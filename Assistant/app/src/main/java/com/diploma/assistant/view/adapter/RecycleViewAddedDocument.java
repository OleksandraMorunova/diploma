package com.diploma.assistant.view.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.model.entity.registration_service.User;
import com.diploma.assistant.service.work_on_files.DownloadFiles;
import com.diploma.assistant.service.work_on_files.GetDataFromUri;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class RecycleViewAddedDocument extends RecyclerView.Adapter<RecycleViewAddedDocument.ViewHolderAddedDocument> implements Serializable {
    private final Activity activity;
    private final List<Uri> items;

    public RecycleViewAddedDocument(Activity activity, List<Uri> items) {
        this.activity = activity;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolderAddedDocument onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAddedDocument(LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.add_task_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAddedDocument holder, int position) {
       SetImageSrcByFormatFile set = new SetImageSrcByFormatFile(activity, items);
       set.putToImage(holder.imageOfFile, position);

        GetDataFromUri get = new GetDataFromUri();
        holder.nameOfFile.setText(get.getFileNameFromUri(activity.getApplicationContext(), items.get(position)));

        holder.itemView.setOnClickListener(v -> {
            DownloadFiles downloadFiles = new DownloadFiles(activity);
            downloadFiles.openConcreteFile(items.get(position));
        });

        holder.deleteItem.setOnClickListener(v -> {
            items.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolderAddedDocument extends RecyclerView.ViewHolder {
        ImageView imageOfFile, deleteItem;
        TextView nameOfFile;

        public ViewHolderAddedDocument(@NonNull View itemView) {
            super(itemView);
            imageOfFile = itemView.findViewById(R.id.image_task_add);
            deleteItem = itemView.findViewById(R.id.text_task_delete);
            nameOfFile = itemView.findViewById(R.id.text_task_add);
        }
    }
}
