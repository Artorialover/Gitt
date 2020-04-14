package com.zhang.netlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.zhang.netlibrary.updateApk.DownloadAPKService;
import com.zhang.netlibrary.updateApk.InstallUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

public class NetUtil {

    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET})
    public static void startDownloadNewApk(Activity activity, @NonNull String destURL,@NonNull String relativePath) throws Exception {
        if(InstallUtils.checkInstallApk(activity,true)){
            Intent intent = new Intent(activity, DownloadAPKService.class);
            intent.putExtra("file_url", destURL);
            intent.putExtra("file_relative_path",relativePath);
            intent.putExtra("isApk",true);
            activity.startService(intent);
        }else {
            throw new Exception("install apk permission is deny");
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET})
    private static void startDownloadFile(Activity activity, @NonNull String destURL,@NonNull String relativePath) throws Exception {
        Intent intent = new Intent(activity, DownloadAPKService.class);
        intent.putExtra("file_url", destURL);
        intent.putExtra("file_relative_path",relativePath);
        intent.putExtra("isApk",false);
        activity.startService(intent);
    }
}
