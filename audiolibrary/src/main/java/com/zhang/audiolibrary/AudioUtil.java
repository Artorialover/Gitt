package com.zhang.audiolibrary;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

public class AudioUtil {
    private static AudioUtil audioUtil;
    private Context context;
    private Handler handler;
    private String TAG="Bus";
    private SpeechSynthesizer mTts;
    private String voiceLocal="xiaoyan";

    public static AudioUtil getInstance(Context context){
        if(audioUtil==null){
            synchronized (AudioUtil.class){
                audioUtil=new AudioUtil(context);
            }
        }
        return audioUtil;
    }
    public AudioUtil(Context context) {
        this.context=context;
        handler=new Handler(Looper.getMainLooper());
        initXunFei(context);
    }

    public void speak(String words){
        if(mTts==null){
            return;
        }
        int code = mTts.startSpeaking(words, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

        //请点击网址https://www.xfyun.cn/document/error-code查询解决方案
        if (code != ErrorCode.SUCCESS) {
            showToast("语音合成失败,错误码: " + code);
        }
    }

    public void pause(){
        if(mTts!=null) {
            mTts.pauseSpeaking();
        }
    }

    public void stop(){
        if(mTts!=null) {
            mTts.stopSpeaking();
        }
    }

    public void resume(){
        if(mTts!=null) {
            mTts.resumeSpeaking();
        }
    }

    public void release(){
        if(mTts!=null) {
            mTts.destroy();
        }
    }

    void initXunFei(final Context context){
        mTts = SpeechSynthesizer.createSynthesizer(context, new InitListener() {
            @Override
            public void onInit(int code) {
                Log.d(TAG, "InitListener init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                    showToast("初始化失败,错误码：" + code );
                } else {
                    // 初始化成功，之后可以调用startSpeaking方法
                    // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                    // 正确的做法是将onCreate中的startSpeaking调用移至这里
                    setParam(context);
                }
            }
        });
    }

    private void setParam(Context context){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成

        //设置使用本地引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        //设置发音人资源路径
        mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath(context));
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME,voiceLocal);

        //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY,"1");//支持实时音频流抛出，仅在synthesizeToUri条件下支持
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "50");
        //	mTts.setParameter(SpeechConstant.STREAM_TYPE, AudioManager.STREAM_MUSIC+"");

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");

        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }

    //获取发音人资源路径
    private String getResourcePath(Context context){
        StringBuffer tempBuffer = new StringBuffer();
        String type= "tts";
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, type+"/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, type + "/" + voiceLocal + ".jet"));

        return tempBuffer.toString();
    }

    SynthesizerListener mTtsListener=new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    void showToast(final String messsage){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,messsage,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
