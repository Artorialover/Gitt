package com.zhang.netlibrary.download;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import androidx.annotation.RequiresPermission;

public class AndroidDownloadManager {

    private DownloadManager downloadManager;
    private Context context;
    private long downloadId;
    private String url;
    private String name;
    private String path;

    private AndroidDownloadManagerListener listener;

    public AndroidDownloadManager(Context context, String url,String fileName,AndroidDownloadManagerListener listener) {
        this.context=context;
        this.url=url;
        this.name=fileName;
        this.listener=listener;

    }

    @RequiresPermission(value = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void download(){
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(name.replace(".apk",""));
        request.setDescription("正在下载");
        request.allowScanningByMediaScanner();
        //设置下载的路径
        //File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), name);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
        request.setDestinationUri(Uri.fromFile(file));
        path = file.getAbsolutePath();

        if (downloadManager == null) {
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        }
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        if (downloadManager != null) {
            if (listener != null) {
                listener.onPrepare();
            }
            downloadId = downloadManager.enqueue(request);
        }
        //注册广播接收者，监听下载状态
        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //广播监听下载的各个状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager.Query query = new DownloadManager.Query();
            //通过下载的id查找
            query.setFilterById(downloadId);
            Cursor cursor = downloadManager.query(query);
            if (cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    //下载暂停
                    case DownloadManager.STATUS_PAUSED:
                        break;
                    //下载延迟
                    case DownloadManager.STATUS_PENDING:
                        break;
                    //正在下载
                    case DownloadManager.STATUS_RUNNING:
                        break;
                    //下载完成
                    case DownloadManager.STATUS_SUCCESSFUL:
                        if (listener != null) {
                            listener.onSuccess(path);
                        }
                        cursor.close();
                        context.unregisterReceiver(receiver);
                        break;
                    //下载失败
                    case DownloadManager.STATUS_FAILED:
                        if (listener != null) {
                            listener.onFailed(new Exception("下载失败"));
                        }
                        cursor.close();
                        context.unregisterReceiver(receiver);
                        break;
                }
            }
        }
    };
}
