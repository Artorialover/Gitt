package com.zhang.netlibrary.updateApk.core;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import androidx.annotation.Nullable;

public class DownloadAPKService extends Service {

    String TAG="DownloadAPKService";
    // 文件保存路径(如果有SD卡就保存SD卡,如果没有SD卡就保存到手机包名下的路径)
    private String APK_dir = "";
    private String APK_path = "";
    private boolean isApk=false;

    @Override
    public void onCreate() {
        super.onCreate();
        initAPKDir();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String APK_URL = intent.getStringExtra("file_url");
        String APK_Relative_Path = intent.getStringExtra("file_relative_path");
        isApk = intent.getBooleanExtra("isApk",false);
        if(!TextUtils.isEmpty(APK_URL) && !TextUtils.isEmpty(APK_Relative_Path)){
            APK_path=APK_dir+APK_Relative_Path;
            downFile(APK_URL,APK_path);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initAPKDir() {
        /**
         * 创建路径的时候一定要用[/],不能使用[\],但是创建文件夹加文件的时候可以使用[\].
         * [/]符号是Linux系统路径分隔符,而[\]是windows系统路径分隔符 Android内核是Linux.
         */
        if (isHasSdcard())// 判断是否插入SD卡
        {
            APK_dir = Environment.getExternalStorageDirectory().getAbsolutePath() ; // 保存到SD卡路径下
        } else {
            APK_dir = getApplicationContext().getFilesDir().getAbsolutePath() ; // 保存到app的包名路径下
        }
        File destDir = new File(APK_dir);
        if (!destDir.exists()) {// 判断文件夹是否存在
            destDir.mkdirs();
        }
    }

    /**
     * @Description 判断是否插入SD卡
     */
    private boolean isHasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void downFile(String file_url, final String target_name){
        RequestParams params=new RequestParams(file_url);
        params.setSaveFilePath(target_name);
        params.setAutoRename(false);

        params.setAutoResume(true);
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Log.d(TAG,"文件下载成功");
                //
                stopSelf();
                if(isApk){
                    InstallUtils.installApk(getApplicationContext(),true,APK_path);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(TAG,"文件下载失败"+ex.getMessage());
                Toast.makeText(getApplicationContext(), "下载失败，请检查网络！", Toast.LENGTH_SHORT).show();
                NotificationUtils.getInstance().cancelDownloadNotification();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d(TAG,"文件下载结束，停止下载器");
                NotificationUtils.getInstance().cancelDownloadNotification();
            }

            @Override
            public void onFinished() {
                Log.d(TAG,"文件下载完成");
                NotificationUtils.getInstance().cancelDownloadNotification();
            }

            @Override
            public void onWaiting() {
                Log.d(TAG,"文件下载处于等待状态");
            }

            @Override
            public void onStarted() {
                Toast.makeText(getApplicationContext(), "开始后台下载更新文件...", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"开始下载文件");
                NotificationUtils.getInstance().init(getApplicationContext());
                NotificationUtils.getInstance().showDownloadNotification();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Log.d(TAG,"文件下载中");
                int x = Long.valueOf(current).intValue();
                int totalS = Long.valueOf(total).intValue();
                NotificationUtils.getInstance().updateProgress(x,totalS);
                //当前进度和文件总大小
                Log.d(TAG,"current：" + current + "，total：" + total);
            }
        });

    }

    private String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
