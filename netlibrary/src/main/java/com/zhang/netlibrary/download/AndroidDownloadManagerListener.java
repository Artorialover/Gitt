package com.zhang.netlibrary.download;

public interface AndroidDownloadManagerListener {

    void onPrepare();

    void onSuccess(String path);

    void onFailed(Throwable throwable);
}
