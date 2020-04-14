package com.zhang.medialibrary.media;

import android.media.MediaPlayer;

import androidx.annotation.NonNull;

public class MediaUtil {
    private static MediaUtil mediaUtil;

    public static MediaUtil getInstance(){
        if(mediaUtil==null){
            synchronized (MediaUtil.class){
                mediaUtil=new MediaUtil();
            }
        }
        return mediaUtil;
    }

    public MediaUtil() {

    }

    //开始
    public void play(@NonNull MediaPlayer mediaplayer){
        mediaplayer.start();
    }

    //暂停
    public void pause(@NonNull MediaPlayer mediaplayer) {
        mediaplayer.pause();
    }

    //判断是否正在播放中
    public boolean isPlay(@NonNull MediaPlayer mediaplayer) {
        return mediaplayer.isPlaying();
    }

    //获取播放时长
    public long getDuring(@NonNull MediaPlayer mediaplayer) {
        // TODO Auto-generated method stub
        return mediaplayer.getDuration();
    }

    //获取当前的播放进度
    public long getCurrentDuring(@NonNull MediaPlayer mediaplayer) {
        // TODO Auto-generated method stub
        return mediaplayer.getCurrentPosition();
    }

    //设置进度..
    public void skipTo(int position, @NonNull MediaPlayer mediaplayer) {
        mediaplayer.seekTo(position);
    }

    //关闭播放器
    public void closeMedia(@NonNull MediaPlayer mediaplayer) {
        if (mediaplayer != null) {
            if (mediaplayer.isPlaying()) {
                mediaplayer.stop();
            }
            mediaplayer.release();
        }

    }
}
