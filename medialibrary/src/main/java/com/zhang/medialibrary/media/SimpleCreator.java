package com.zhang.medialibrary.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class SimpleCreator {
    public static MediaPlayer create(Context context) throws IOException {
        AssetManager assetManager=context.getAssets();
        AssetFileDescriptor assetFileDescriptor = assetManager.openFd("music.mp3");
        MediaPlayer mediaPlayer=new MediaPlayer();
        mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
        mediaPlayer.prepare();
        return mediaPlayer;
    }
}
