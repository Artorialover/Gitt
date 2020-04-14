package com.bus.voicebroadcast.autostart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bus.voicebroadcast.MainActivity;

public class RebootBroadcastReceiver extends BroadcastReceiver {
    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * 如果系统启动的广播，则启动 APP主页活动
         */
        if (ACTION_BOOT.equals(intent.getAction())) {
            Intent intentMainActivity = new Intent(context, MainActivity.class);
            intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentMainActivity);
        }
    }
}
