package com.diploma.assistant.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.assistant.R;
import com.diploma.assistant.model.entity.registration_service.LoadFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class RecycleViewLookDocument extends RecyclerView.Adapter<RecycleViewLookDocument.ViewHolderLookDocument> implements Serializable {
    private final Activity activity;
    private final List<LoadFile> items;

    public RecycleViewLookDocument(Activity activity, List<LoadFile> items) {
        this.activity = activity;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolderLookDocument onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderLookDocument(LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.look_task_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLookDocument holder, int position) {
        List<Uri> uris = new ArrayList<>();
        for(int i = 0; i < items.size(); i++){
            uris.add(getUriList(items.get(i)));
        }

        if(items.get(position) != null){
            holder.nameOfFile.setText(items.get(position).getFilename());
            SetImageSrcByFormatFile set = new SetImageSrcByFormatFile(activity, uris);
            set.putToImage(holder.imageOfFile, position);

            holder.imageOfFile.setOnClickListener(s -> {
                byte[] bytes = Base64.getDecoder().decode(items.get(position).getFile());
                openFile(bytes, items.get(position).getFilename(), items.get(position).getFileType());
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    
    private Uri getUriList(LoadFile l){
        if(l != null && l.getFilename() != null){
            File file = new File(activity.getCacheDir(), l.getFilename());
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(file);
                byte[] bytes = Base64.getDecoder().decode(l.getFile());
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();
                return FileProvider.getUriForFile(activity.getApplicationContext(), activity.getPackageName() + ".fileprovider", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void openFile(byte[] fileBytes, String fileName, String mimeType) {
        try {
            File file = new File(activity.getCacheDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileBytes);
            outputStream.flush();
            outputStream.close();
            Uri uri = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getPackageName() + ".fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, mimeType);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ViewHolderLookDocument extends RecyclerView.ViewHolder {
        ImageView imageOfFile;
        TextView nameOfFile;

        public ViewHolderLookDocument(@NonNull View itemView) {
            super(itemView);
            imageOfFile = itemView.findViewById(R.id.image_task_look);
            nameOfFile = itemView.findViewById(R.id.text_task_look);
        }
    }
}