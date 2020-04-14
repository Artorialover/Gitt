package com.zhang.audiolibrary;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class AudioApplication implements IAudioComponentApplication {
    @Override
    public void init(Application application) {
        System.out.println("初始化AudioApplication");
        StringBuffer param = new StringBuffer();
        param.append("appid="+"5e86c6dc");

        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);

        SpeechUtility.createUtility(application,param.toString());
    }
}
