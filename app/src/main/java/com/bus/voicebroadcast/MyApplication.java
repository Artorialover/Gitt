package com.bus.voicebroadcast;

import android.app.Application;

import com.zhang.audiolibrary.IAudioComponentApplication;
import com.zhang.netlibrary.updateApk.INetComponentApplication;
import com.zhang.splashmodule.SplashModule;


public class MyApplication extends Application {

    private String[] modules=new String[]{"com.zhang.netlibrary.updateApk.NetApplication","com.zhang.audiolibrary.AudioApplication"};

    @Override
    public void onCreate() {
        initModules();
        SplashModule.init(true, MainActivity.class);
        super.onCreate();
    }

    void initModules(){
        for (String module : modules) {
            try {
                Class<?> aClass = Class.forName(module);
                Object o = aClass.newInstance();
                if(o instanceof INetComponentApplication){
                    ((INetComponentApplication) o).init(this);
                }else if(o instanceof IAudioComponentApplication){
                    ((IAudioComponentApplication) o).init(this);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
