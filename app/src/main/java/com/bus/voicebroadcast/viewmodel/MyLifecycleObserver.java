package com.bus.voicebroadcast.viewmodel;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class MyLifecycleObserver implements LifecycleObserver {

    private String TAG="voicebroadcast";
    private Lifecycle lifecycle;
    public MyLifecycleObserver(Lifecycle lifecycle) {
        this.lifecycle=lifecycle;
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_CREATE)
    public void activityCreate(){
        Log.d(TAG, "activityCreate ");
        if(lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)){
            Log.d(TAG, "Lifecycle.State is greater than START ");
        }else {
            Log.d(TAG, "Lifecycle.State is less than START ");
        }
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
    public void activityStart(){
        Log.d(TAG, "activityStart ");

    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    public void activityDestroy(){
        Log.d(TAG, "activityDestroy ");
    }
}
