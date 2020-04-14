package com.zhang.netlibrary.updateApk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class InstallUtils {
    private static final String TAG="netlibrary";

    public final static int REQUEST_UNKNOWN_APP=2020;

    /**
     * 安装app
     * @param context 上下文
     * @param request 如果没有安装未知来源App的权限，是否申请
     * @param path 目标App路径
     */
    public static void installApk(@NonNull Context  context, boolean request, @NonNull String path){
        //检查安装未知应用的权限

        String authorities = getAuthorities(context);
        if(authorities==null){
            Log.d(TAG,"install: authorities is null");
            return;
        }

        if(!checkInstallApk(context,request)){
            Log.d(TAG,"install: permission deny");
            return;
        }
        Log.d(TAG,"install: "+path);
        File pFile=new File(path);
        if(!pFile.exists()  || !pFile.isFile()){
            Log.d(TAG,"install: file is invalid");
            return;
        }
        Intent intent=new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri= FileProvider.getUriForFile(context,authorities,pFile);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        }else {
            uri = Uri.fromFile(pFile);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private static String getAuthorities(Context context){
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PROVIDERS);
            ProviderInfo[] providers = packageInfo.providers;
            if(providers!=null && providers.length!=0 && providers[0].authority!=null){
                return providers[0].authority;
            }else {
                return null;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 检查安装未知来源App的权限
     * @param activity 上下文
     * @param request 如果没有安装未知来源App的权限，是否申请
     * @return
     */
    public static boolean checkInstallApk(@NonNull Context activity, boolean request){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            PackageManager packageManager = activity.getPackageManager();
            boolean b = packageManager.canRequestPackageInstalls();
            if(!b){
                if(request) {
                    Uri packageUri = Uri.parse("package:" + getPackageName(activity));
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 获取当前APP包名
     * @param context 上下文
     * @return APP包名
     */
    public static String getPackageName(Context context){
        PackageManager manager=context.getPackageManager();
        try {
            PackageInfo info=manager.getPackageInfo(context.getPackageName(),0);
            return info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getVersionCode(Context context){
        PackageManager manager=context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return  packageInfo.getLongVersionCode();
            }else {
                return  packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
