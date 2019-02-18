package com.nibiru.billingkaraoke.utils;

import android.content.Context;
import android.media.MediaPlayer;


public class PlaySound {
    public static final void playsound(Context ctx,int fileplay){
        final MediaPlayer mediaPlayer = MediaPlayer.create(ctx,fileplay);
        mediaPlayer.start();
    }

}
