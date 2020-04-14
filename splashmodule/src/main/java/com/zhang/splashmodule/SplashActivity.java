package com.zhang.splashmodule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zhang.splashmodule.log.LogUtil;
import com.zhang.splashmodule.util.SPUtil;
import com.zhang.splashmodule.util.SystemUiUtil;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private Context mContext;
    private long TIME_COUNTDOWN=1500;
    private long TIME_COUNTDOWN_LONG=3500;
    private Handler handler=new Handler(Looper.getMainLooper());

    private ImageView ivLaunch;
    private ImageView ivLaunchLogo;
    private Button btnSkip;
    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        mContext=this;
        SystemUiUtil.setSystemUi(this,true,true,true,true);

        initWidget();
        init();
    }

    private void initWidget(){
        ivLaunch=findViewById(R.id.iv_launch_main);
        ivLaunchLogo=findViewById(R.id.iv_launch_logo);
        btnSkip=findViewById(R.id.btn_skip);

    }

    private void init() {
        LogUtil.d("launch image start: ");
        //如果不从网络加载图片，不显示按钮，并且缩短展示时间
        if(SplashModule.isLoadLocal()){
            //不显示按钮
            setSkipVisibility(false);
        }else {
            setSkipVisibility(SplashModule.isShowBtnSkip());
        }
        //加载公司logo
        Glide.with(this).load(R.drawable.ic_logo_shape).into(ivLaunchLogo);
        //加载主体图片
        if(SplashModule.isLoadLocal()){
            Glide.with(this).load(R.drawable.ic_logo_text)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            LogUtil.d("launch image fail: " + e.getMessage());
                            toNextActivity(1000);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            LogUtil.d("launch image success");
                            setCountDown();
                            return false;
                        }
                    })
                    .into(ivLaunch);
        }else {
            //设置展示时间
            TIME_COUNTDOWN=TIME_COUNTDOWN_LONG;

            if(TextUtils.isEmpty(SplashModule.getUri())){
                toNextActivity(1000);
                return;
            }
            //从网络加载图片
            Glide.with(this).load(SplashModule.getUri())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            LogUtil.d("launch image fail: " + e.getMessage());
                            toNextActivity(1000);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            LogUtil.d("launch image succ");
                            setCountDown();
                            return false;
                        }
                    })
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivLaunch);
        }

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer!=null){
                    timer.cancel();
                    timer.onFinish();
                }else {
                    toNextActivity(200);
                }
            }
        });
    }

    void setSkipVisibility(final boolean visible){
        handler.post(new Runnable() {
            @Override
            public void run() {
                btnSkip.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }


    void setCountDown() {
        timer=new CountDownTimer(TIME_COUNTDOWN,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setSkipText(millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                toNextActivity(200);
            }
        };
        timer.start();
    }

    void setSkipText(final long time){
        handler.post(new Runnable() {
            @Override
            public void run() {
                String s="还剩"+"<font color='#C226DD'>" + String.format(Locale.US,"%d",time) + "</font>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    btnSkip.setText(Html.fromHtml(s,Html.FROM_HTML_MODE_LEGACY));
                }else {
                    btnSkip.setText(Html.fromHtml(s));
                }
            }
        });
    }

    private void toNextActivity(long delay) {
        if(SplashModule.isShowGuidePage() && SPUtil.isFirstRun(mContext)){
            LogUtil.d("to GuideActivity");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mContext, GuideActivity.class );
                    startActivity(intent);
                    finish();
                }
            },delay);
        }else {
            LogUtil.d("to HomeActivity");
            if(SplashModule.getNextActivity()==null){
                throw new RuntimeException("SplashModule.init() method should invoke");
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mContext,SplashModule.getNextActivity());
                    startActivity(intent);
                    finish();
                }
            },delay);
        }
    }
}
