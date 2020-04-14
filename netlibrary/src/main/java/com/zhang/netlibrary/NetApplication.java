package com.zhang.netlibrary;

import android.app.Application;

import org.xutils.x;

public class NetApplication implements INetComponentApplication {
    @Override
    public void init(Application application) {
        System.out.println("初始化NetApplication");
        x.Ext.init(application);
    }
}
