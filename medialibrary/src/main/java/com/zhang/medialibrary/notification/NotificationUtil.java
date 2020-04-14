package com.zhang.medialibrary.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class NotificationUtil {
    private static NotificationUtil ourInstance;

    public static NotificationUtil getInstance(@NonNull Context context) {
        if(ourInstance==null){
            synchronized (NotificationUtil.class){
                ourInstance=new NotificationUtil(context);
            }
        }
        return ourInstance;
    }

    private Context context;
    private static NotificationManager manager;

    private final String CHANNEL_ONE_ID="Notify_1";
    private final String CHANNEL_ONE_NAME="普通通知";
    private NotificationCompat.Builder builder;

    private boolean bigText=false;
    private boolean bigPicture=false;
    private NotificationUtil(Context context) {
        this.context=context;
        manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void remove(int uniqueId){
        manager.cancel(uniqueId);
    }

    public void showNotification(int uniqueId, String title, String content, @DrawableRes int smallIcon, @DrawableRes int largeIcon, PendingIntent pi){
        if(manager==null){
            throw new RuntimeException("NotificationManager is null");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name;
            NotificationChannel channel=new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME,NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }
        builder=new NotificationCompat.Builder(context, CHANNEL_ONE_ID);
        builder.setContentTitle(title);
        if(bigText){
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(content));
        }else {
            builder.setContentText(content);
        }
        builder.setSmallIcon(smallIcon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),largeIcon));
        if(pi!=null){
            builder.setContentIntent(pi);
            builder.setAutoCancel(true);
        }
        Notification notification = builder.build();
        manager.notify(uniqueId,notification);
    }

    public void setBigTextStyle(boolean enable){
        bigText=enable;
    }

    private void setBigPictureStyle(boolean enable){
        bigPicture=enable;
    }

}
