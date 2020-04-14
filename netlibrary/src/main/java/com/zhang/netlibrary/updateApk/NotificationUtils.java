package com.zhang.netlibrary.updateApk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import com.zhang.netlibrary.R;

import java.text.DecimalFormat;

import androidx.core.app.NotificationCompat;

public class NotificationUtils {
    private static final NotificationUtils ourInstance = new NotificationUtils();

    public static NotificationUtils getInstance() {
        return ourInstance;
    }

    private Context context;
    private static NotificationManager manager;

    private final String CHANNEL_ONE_ID="Notify_1";
    private final String CHANNEL_ONE_NAME="Download";
    private final int NOTIFY_ID=102;
    private NotificationCompat.Builder builder;

    private NotificationUtils() {
    }

    public void init(Context context){
        this.context=context;
        manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public NotificationManager getManager(){
        return manager;
    }

    public void showDownloadNotification(){
        if(getManager()==null){
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel  channel=new NotificationChannel(CHANNEL_ONE_ID,CHANNEL_ONE_NAME,NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(channel);
        }
        builder=new NotificationCompat.Builder(context);
        builder.setChannelId(CHANNEL_ONE_ID);
        builder.setSmallIcon(R.drawable.download);
        builder.setContentTitle("版本更新");
        builder.setContentText("正在下载");
        builder.setWhen(System.currentTimeMillis());
        builder.setProgress(100,0,false);
        getManager().notify(NOTIFY_ID,builder.build());
    }

    void updateProgress(int progress){
        if(builder!=null && getManager()!=null){
            builder.setProgress(100,progress,false);
            builder.setContentText("下载"+progress+"%");
            getManager().notify(NOTIFY_ID,builder.build());
        }
    }

    public void updateProgress(int x,int total){
        if(builder!=null && getManager()!=null){
            builder.setProgress(total,x,false);
            builder.setContentText("已下载"+getPercent(x, total));
            getManager().notify(NOTIFY_ID,builder.build());
        }
    }

    private String getPercent(int x, int total) {
        String result = "";// 接受百分比的值
        double x_double = x * 1.0;
        double tempresult = x_double / total;
        // 百分比格式，后面不足2位的用0补齐 ##.0%
        DecimalFormat df1 = new DecimalFormat("0.0%");
        result = df1.format(tempresult);
        return result;
    }

    void cancelDownloadNotification(){
        if(getManager()!=null){
            getManager().cancel(NOTIFY_ID);
        }
    }
}
