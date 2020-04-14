package com.bus.voicebroadcast.ble;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import com.bus.voicebroadcast.debug.LogUtil;

import androidx.annotation.Nullable;

public class ScanService extends Service {

    private final String CHANNEL_ONE_ID="channel_1";
    private final String CHANNEL_ONE_NAME="BLE_FOREGROUND_SERVICE";
    private Handler handler=new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground();
        LogUtil.d("ScanService onCreate");
        BleUtil.getInstance(getApplicationContext()).startLeScan();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long one=0;
                LogUtil.d("ScanService Thread run");
                while (true){
                    try {
                        LogUtil.d("ScanService Thread "+one);
                        one++;
                        if(one==Long.MAX_VALUE){
                            one=0;
                        }
                        showToast("计数："+one);
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("ScanService onCreate");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startForeground(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel= new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            Intent intent = new Intent();
            PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, intent, 0);
            Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
            builder.setChannelId(CHANNEL_ONE_ID)
                    .setOngoing(false)
                    .setContentTitle("车载蓝牙扫描") // 设置下拉列表里的标题
                    .setContentText("") // 设置上下文内容
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent); // 设置该通知发生的时间
            Notification notification = builder.build(); // 获取构建好的Notification
            startForeground(111,notification);
        }else {
            Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
            builder.setOngoing(false).setContentTitle("BLE调试助手") // 设置下拉列表里的标题
                    .setContentText("") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
            Notification notification = builder.build(); // 获取构建好的Notification
            startForeground(1,notification);
        }

    }

    void showToast(final String message){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
