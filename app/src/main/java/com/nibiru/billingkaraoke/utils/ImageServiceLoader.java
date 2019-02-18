package com.nibiru.billingkaraoke.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class ImageServiceLoader {
    public static void LoadImage(Context context, String url,int error,ImageView img) {
//        final ObjectKey objectKey = new ObjectKey(String.valueOf(System.currentTimeMillis()));
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .error(error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .skipMemoryCache( true )
                        .centerCrop()
                        .placeholder(error)
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(img);
    }
}
