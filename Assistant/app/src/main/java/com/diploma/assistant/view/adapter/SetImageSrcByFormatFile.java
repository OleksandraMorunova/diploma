package com.diploma.assistant.view.adapter;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import com.diploma.assistant.R;

import java.util.List;
import java.util.Objects;

public class SetImageSrcByFormatFile {
    private final Activity activity;
    private final List<Uri> items;

    public SetImageSrcByFormatFile(Activity activity, List<Uri> items) {
        this.activity = activity;
        this.items = items;
    }

    public void putToImage(ImageView imageOfFile, int position){
        String format = activity.getContentResolver().getType(items.get(position));
        if(Objects.equals(format, "image/png") || Objects.equals(format, "image/jpeg") || Objects.equals(format, "image/bitmap")){
            imageOfFile.setImageURI(items.get(position));
        } else if(format.equals("application/pdf")){
            imageOfFile.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_baseline_pdf,null));
        } else if(format.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            imageOfFile.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_baseline_doc, null));
        } else if (format.equals("video/mp4")){
            imageOfFile.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_baseline_video_mp4, null));
        } else if(format.equals("audio/mpeg")){
            imageOfFile.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_baseline_audio, null));
        } else if(format.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
            imageOfFile.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_baseline_xls, null));
        } else if(format.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")){
            imageOfFile.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_baseline_ppt, null));
        } else {
            imageOfFile.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_baseline_image_not_supported, null));
        }
    }
}